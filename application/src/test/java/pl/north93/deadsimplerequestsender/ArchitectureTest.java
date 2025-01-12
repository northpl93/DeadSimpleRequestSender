package pl.north93.deadsimplerequestsender;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;


import com.google.common.eventbus.Subscribe;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.syntax.elements.MethodsShouldConjunction;
import com.tngtech.archunit.library.dependencies.SliceRule;

@AnalyzeClasses(packages = "pl.north93.deadsimplerequestsender")
public class ArchitectureTest
{
    @ArchTest
    public void thereShouldBeNoCyclicDependencies(final JavaClasses classes)
    {
        final SliceRule archRule =
                slices().matching("pl.north93.deadsimplerequestsender.(*)..").should().beFreeOfCycles();

        archRule.check(classes);
    }

    @ArchTest
    public void subscribeMethodsShouldFollowRules(final JavaClasses classes)
    {
        final MethodsShouldConjunction archRule =
                methods().that().areAnnotatedWith(Subscribe.class)
                         .should().bePrivate()
                         .andShould().haveNameStartingWith("handle");

        archRule.check(classes);
    }

    @ArchTest
    public void noClassesShouldAccessStandardStreams(final JavaClasses classes)
    {
        NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.check(classes);
    }

    @ArchTest
    public void noClassesShouldUseJavaUtilLogging(final JavaClasses classes)
    {
        NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING.check(classes);
    }
}
