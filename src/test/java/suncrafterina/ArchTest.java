package suncrafterina;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("suncrafterina");

        noClasses()
            .that()
                .resideInAnyPackage("suncrafterina.service..")
            .or()
                .resideInAnyPackage("suncrafterina.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..suncrafterina.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
