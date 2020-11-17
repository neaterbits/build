package com.neaterbits.build.buildsystem.maven.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.build.buildsystem.maven.elements.MavenReportPlugin;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class ReportingParserTest extends BasePomParserTest {

    @Test
    public void testReporting() throws IOException, XMLReaderException {
    
        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";
    
        final String pomString =
                "<project>"
    
              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"
    
              + "  <reporting>"
              + "    <plugins>"
              + "      <plugin>"
              
              + "         <groupId>pluginGroupId</groupId>"
              + "         <artifactId>pluginArtifactId</artifactId>"
              + "         <version>pluginVersion</version>"
              
              + "         <reportSets>"
              + "           <reportSet>"
              + "             <id>testReportSet</id>"

              + "             <reports>"
              + "               <report>testReport1</report>"
              + "               <report>testReport2</report>"
              + "               <report>testReport3</report>"
              + "             </reports>"

              + "           </reportSet>"
              + "         </reportSets>"

              + "      </plugin>"
              + "    </plugins>"
              + "  </reporting>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        final MavenReporting reporting = project.getCommon().getReporting();
        
        assertThat(reporting.getPlugins().size()).isEqualTo(1);
        
        final MavenReportPlugin plugin = reporting.getPlugins().get(0);

        assertThat(plugin.getModuleId().getGroupId()).isEqualTo("pluginGroupId");
        assertThat(plugin.getModuleId().getArtifactId()).isEqualTo("pluginArtifactId");
        assertThat(plugin.getModuleId().getVersion()).isEqualTo("pluginVersion");
    
        assertThat(plugin.getReportSets().size()).isEqualTo(1);

        assertThat(plugin.getReportSets().get(0).getId()).isEqualTo("testReportSet");

        assertThat(plugin.getReportSets().get(0).getReports().size()).isEqualTo(3);

        assertThat(plugin.getReportSets().get(0).getReports().get(0)).isEqualTo("testReport1");
        assertThat(plugin.getReportSets().get(0).getReports().get(1)).isEqualTo("testReport2");
        assertThat(plugin.getReportSets().get(0).getReports().get(2)).isEqualTo("testReport3");
    }
}
