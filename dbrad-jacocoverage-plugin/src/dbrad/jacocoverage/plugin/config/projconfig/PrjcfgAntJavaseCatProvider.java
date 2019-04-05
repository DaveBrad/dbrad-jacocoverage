/* 
 * Copyright (c) 2017 dbradley.
 * Structural changes implemented around flow to interact with new code:
 *
 * Author: Jonathan Lermitage 2016.
 * Contains original code from tikione-coverage authors under WTFPL.
 */
package dbrad.jacocoverage.plugin.config.projconfig;

import org.netbeans.spi.project.ui.support.ProjectCustomizer;

/**
 * JaCoCoverage configuration category at project level.
 * <br>See
 * <a href="http://wiki.netbeans.org/DevFaqActionAddProjectCustomizer">DevFaqActionAddProjectCustomizer</a>
 * for integration.
 *
 * @author Jonathan Lermitage (pre-2017)
 * @author dbradley (2017)
 */
@ProjectCustomizer.CompositeCategoryProvider.Registrations({
    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = "org-netbeans-modules-java-j2seproject",
            position = 1400)
    ,
	@ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = "org-netbeans-modules-apisupport-project",
            position = 1401)
    ,
    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType
            = "org-netbeans-modules-apisupport-project-suite", // .SuiteProject",
            position = 1402)
})
public class PrjcfgAntJavaseCatProvider extends PrjcfgAntAbstractCatProvider
        implements ProjectCustomizer.CompositeCategoryProvider {
}
