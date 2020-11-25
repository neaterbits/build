package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReportPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReporting;
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

              + "         <inherited>false</inherited>"

              + "         <configuration>"

              + "           <confKey>value</confKey>"

              + "           <listKey>"
              + "              <item>item1</item>"
              + "              <item>item2</item>"
              + "              <item>item3</item>"
              + "           </listKey>"

              + "           <objectKey>"
              + "              <field1>value1</field1>"
              + "              <field2>value2</field2>"
              + "              <field3>value3</field3>"
              + "           </objectKey>"
              + "         </configuration>"

              + "         <reportSets>"
              + "           <reportSet>"
              + "             <id>testReportSet</id>"

              + "             <reports>"
              + "               <report>testReport1</report>"
              + "               <report>testReport2</report>"
              + "               <report>testReport3</report>"
              + "             </reports>"
              
              + "             <configuration>"
              + "                <reportConfKey>reportConfValue</reportConfKey>"
              + "             </configuration>"

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

        assertThat(plugin.getConfiguration().getInherited()).isFalse();

        assertThat(plugin.getConfiguration().getMap().getKeys()).containsOnly("confKey", "listKey", "objectKey");

        assertThat(plugin.getConfiguration().getMap().getString("confKey")).isEqualTo("value");
        assertThat(plugin.getConfiguration().getMap().getSubObject("listKey").getStringList("item")).containsExactly("item1", "item2", "item3");
        assertThat(plugin.getConfiguration().getMap().getSubObject("objectKey").getString("field1")).isEqualTo("value1");
        assertThat(plugin.getConfiguration().getMap().getSubObject("objectKey").getString("field2")).isEqualTo("value2");
        assertThat(plugin.getConfiguration().getMap().getSubObject("objectKey").getString("field3")).isEqualTo("value3");
        
        assertThat(plugin.getReportSets().size()).isEqualTo(1);

        assertThat(plugin.getReportSets().get(0).getId()).isEqualTo("testReportSet");

        assertThat(plugin.getReportSets().get(0).getReports().size()).isEqualTo(3);

        assertThat(plugin.getReportSets().get(0).getReports().get(0)).isEqualTo("testReport1");
        assertThat(plugin.getReportSets().get(0).getReports().get(1)).isEqualTo("testReport2");
        assertThat(plugin.getReportSets().get(0).getReports().get(2)).isEqualTo("testReport3");

        assertThat(plugin.getReportSets().get(0).getConfiguration().getMap().getKeys()).containsOnly("reportConfKey");
        assertThat(plugin.getReportSets().get(0).getConfiguration().getMap().getValue("reportConfKey")).isEqualTo("reportConfValue");
    }
}
