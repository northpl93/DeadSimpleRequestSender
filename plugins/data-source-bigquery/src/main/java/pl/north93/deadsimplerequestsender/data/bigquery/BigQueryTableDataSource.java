package pl.north93.deadsimplerequestsender.data.bigquery;

import static pl.north93.deadsimplerequestsender.data.bigquery.ArrowSchemaReader.convertArrowSchemaToDataHeader;
import static pl.north93.deadsimplerequestsender.data.bigquery.ArrowSchemaReader.readArrowSchemaFromBigQuery;


import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigquery.storage.v1.BigQueryReadClient;
import com.google.cloud.bigquery.storage.v1.CreateReadSessionRequest;
import com.google.cloud.bigquery.storage.v1.ReadRowsRequest;
import com.google.cloud.bigquery.storage.v1.ReadRowsResponse;
import com.google.cloud.bigquery.storage.v1.ReadSession;
import com.google.cloud.bigquery.storage.v1.ReadStream;

import org.apache.arrow.vector.types.pojo.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.DataSource;

public class BigQueryTableDataSource implements DataSource
{
    private static final Logger log = LoggerFactory.getLogger(BigQueryTableDataSource.class);
    private final BigQueryReadClient bigQueryReadClient;
    private final ReadSession readSession;
    private final DataHeader dataHeader;
    private final StreamingContextsHolder streamingContextsHolder;

    public BigQueryTableDataSource(final BigQueryReadClient bigQueryReadClient, final CreateReadSessionRequest createReadSessionRequest)
    {
        this.bigQueryReadClient = bigQueryReadClient;
        this.readSession = bigQueryReadClient.createReadSession(createReadSessionRequest);
        log.info("Created BigQuery read session, will expire at {}", this.getSessionExpireTime());

        final Schema arrowSchema = readArrowSchemaFromBigQuery(this.readSession.getArrowSchema());
        this.dataHeader = convertArrowSchemaToDataHeader(arrowSchema);
        this.streamingContextsHolder = new StreamingContextsHolder(this.openStreamingContexts(bigQueryReadClient, this.readSession, arrowSchema));
    }

    private Instant getSessionExpireTime()
    {
        return Instant.ofEpochSecond(this.readSession.getExpireTime().getSeconds());
    }

    private Collection<RowStreamingContext> openStreamingContexts(final BigQueryReadClient bigQueryReadClient, final ReadSession readSession, final Schema arrowSchema)
    {
        return readSession.getStreamsList().stream().map(this.openStreamingContext(bigQueryReadClient, arrowSchema)).toList();
    }

    private Function<ReadStream, RowStreamingContext> openStreamingContext(final BigQueryReadClient bigQueryReadClient, final Schema arrowSchema)
    {
        return readStream ->
        {
            log.info("Creating new streaming context for stream {}", readStream.getName());
            final ReadRowsRequest readRowsRequest = ReadRowsRequest.newBuilder().setReadStream(readStream.getName()).build();
            final ServerStream<ReadRowsResponse> serverStream = bigQueryReadClient.readRowsCallable().call(readRowsRequest);
            return new RowStreamingContext(readStream.getName(), arrowSchema, serverStream);
        };
    }

    @Override
    public DataHeader readHeader()
    {
        return this.dataHeader;
    }

    @Override
    public Map<String, Object> metadata()
    {
        final long estimatedRowCount = this.readSession.getEstimatedRowCount();
        return Map.of(
                "timeout", this.getSessionExpireTime(),
                "estimatedRowCount", estimatedRowCount
        );
    }

    @Override
    public boolean hasMore()
    {
        return this.streamingContextsHolder.hasAnyStreamingContext();
    }

    @Override
    public Collection<DataRow> readBatch(final int batchSize)
    {
        return this.streamingContextsHolder.readBatchFromStream(batchSize);
    }

    @Override
    public void close()
    {
        this.streamingContextsHolder.close();
        this.bigQueryReadClient.close();
    }
}
