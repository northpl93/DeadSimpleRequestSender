package pl.north93.deadsimplerequestsender.data.bigquery.config;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.cloud.bigquery.storage.v1.BigQueryReadClient;
import com.google.cloud.bigquery.storage.v1.CreateReadSessionRequest;
import com.google.cloud.bigquery.storage.v1.DataFormat;
import com.google.cloud.bigquery.storage.v1.ReadSession;
import com.google.cloud.bigquery.storage.v1.ReadSession.TableReadOptions;
import com.google.inject.Injector;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceConfig;
import pl.north93.deadsimplerequestsender.data.bigquery.BigQueryTableDataSource;

@JsonTypeName("bigquery-table")
public record BigQueryTableDataSourceConfig(
        String billingProject,
        TableConfig table,
        List<String> fields,
        String rowRestriction
) implements DataSourceConfig
{
    @Override
    public DataSource createDataSource(final File localWorkDir, final Injector injector)
    {
        final BigQueryReadClient client;
        try
        {
            client = BigQueryReadClient.create();
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

        final ReadSession.Builder sessionBuilder =
                ReadSession.newBuilder()
                           .setTable(this.table.formatResourceName())
                           .setDataFormat(DataFormat.ARROW)
                           .setReadOptions(this.buildTableReadOptions());

        final CreateReadSessionRequest.Builder builder =
                CreateReadSessionRequest.newBuilder()
                                        .setParent(this.formatParentProjectResourceName())
                                        .setReadSession(sessionBuilder)
                                        .setPreferredMinStreamCount(20);

        return new BigQueryTableDataSource(client, builder.build());
    }

    private String formatParentProjectResourceName()
    {
        return MessageFormat.format("projects/{0}", this.billingProject);
    }

    private TableReadOptions buildTableReadOptions()
    {
        final TableReadOptions.Builder options = TableReadOptions.newBuilder();
        this.fields.forEach(options::addSelectedFields);
        if (this.rowRestriction != null)
        {
            options.setRowRestriction(this.rowRestriction);
        }

        return options.build();
    }
}

