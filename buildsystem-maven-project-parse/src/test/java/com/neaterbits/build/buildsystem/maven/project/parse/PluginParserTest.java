package com.neaterbits.build.buildsystem.maven.project.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.project.model.MavenBuild;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuildPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;

public class PluginParserTest extends BasePomParserTest {

    @Test
    public void testPlugins() throws IOException, XMLReaderException {
    
        final String groupId = "theGroupId";
        final String artifactId = "theArtifactId";
        final String version = "theVersion";
    
        final String pomString =
                "<project>"
    
              + "  <groupId>" + groupId + "</groupId>"
              + "  <artifactId>" + artifactId + "</artifactId>"
              + "  <version>" + version + "</version>"
    
              + "  <build>"
              + "    <plugins>"
              + "      <plugin>"
              
              + "         <groupId>pluginGroupId</groupId>"
              + "         <artifactId>pluginArtifactId</artifactId>"
              + "         <version>pluginVersion</version>"
              
              + "         <extensions>true</extensions>"
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
              
              + "         <dependencies>"
              + "           <dependency>"
              + "             <groupId>pluginDependencyGroupId</groupId>"
              + "             <artifactId>pluginDependencyArtifactId</artifactId>"
              + "             <version>pluginDependencyVersion</version>"
              + "           </dependency>"
              + "         </dependencies>"

              + "         <executions>"
              + "           <execution>"
              + "             <id>testExecution</id>"

              + "             <goals>"
              + "               <goal>testGoal</goal>"
              + "             </goals>"

              + "             <phase>testPhase</phase>"
              + "             <inherited>false</inherited>"

              + "             <configuration>"
              + "               <confKey>value</confKey>"

              + "               <listKey>"
              + "                 <objectKey>"
              + "                   <field1>obj1value1</field1>"
              + "                   <field2>obj1value2</field2>"
              + "                   <field3>obj1value3</field3>"
              + "                 </objectKey>"
              + "                 <objectKey>"
              + "                   <field1>obj2value1</field1>"
              + "                   <field2>obj2value2</field2>"
              + "                   <field3>obj2value3</field3>"
              + "                 </objectKey>"
              + "                 <objectKey>"
              + "                   <field1>obj3value1</field1>"
              + "                   <field2>obj3value2</field2>"
              + "                   <field3>obj3value3</field3>"
              + "                 </objectKey>"
              + "               </listKey>"
              
              + "             </configuration>"
              + "           </execution>"
              + "         </executions>"

              + "      </plugin>"
              + "    </plugins>"
              + "  </build>"

              + "</project>";

        final MavenProject project = parse(pomString);
        
        final MavenBuild build = project.getCommon().getBuild();
        
        assertThat(build.getPlugins().size()).isEqualTo(1);
        
        final MavenBuildPlugin plugin = build.getPlugins().get(0);

        assertThat(plugin.getModuleId().getGroupId()).isEqualTo("pluginGroupId");
        assertThat(plugin.getModuleId().getArtifactId()).isEqualTo("pluginArtifactId");
        assertThat(plugin.getModuleId().getVersion()).isEqualTo("pluginVersion");
    
        assertThat(plugin.getExtensions()).isTrue();
        
        assertThat(plugin.getConfiguration().getInherited()).isFalse();

        assertThat(plugin.getConfiguration().getMap().getKeys()).containsOnly("confKey", "listKey", "objectKey");

        assertThat(plugin.getConfiguration().getMap().getString("confKey")).isEqualTo("value");
        assertThat(plugin.getConfiguration().getMap().getSubObject("listKey").getStringList("item")).containsExactly("item1", "item2", "item3");
        assertThat(plugin.getConfiguration().getMap().getSubObject("objectKey").getString("field1")).isEqualTo("value1");
        assertThat(plugin.getConfiguration().getMap().getSubObject("objectKey").getString("field2")).isEqualTo("value2");
        assertThat(plugin.getConfiguration().getMap().getSubObject("objectKey").getString("field3")).isEqualTo("value3");
        
        assertThat(plugin.getDependencies().size()).isEqualTo(1);
        assertThat(plugin.getDependencies().get(0).getModuleId().getGroupId()).isEqualTo("pluginDependencyGroupId");
        assertThat(plugin.getDependencies().get(0).getModuleId().getArtifactId()).isEqualTo("pluginDependencyArtifactId");
        assertThat(plugin.getDependencies().get(0).getModuleId().getVersion()).isEqualTo("pluginDependencyVersion");

        assertThat(plugin.getExecutions().size()).isEqualTo(1);
        assertThat(plugin.getExecutions().get(0).getId()).isEqualTo("testExecution");
        assertThat(plugin.getExecutions().get(0).getGoals().size()).isEqualTo(1);
        assertThat(plugin.getExecutions().get(0).getGoals().get(0)).isEqualTo("testGoal");
        assertThat(plugin.getExecutions().get(0).getPhase()).isEqualTo("testPhase");
        assertThat(plugin.getExecutions().get(0).getConfiguration().getInherited()).isFalse();
        
        assertThat(plugin.getExecutions().get(0).getConfiguration().getMap().getKeys()).containsOnly("confKey", "listKey");
                
        assertThat(plugin.getExecutions().get(0).getConfiguration().getMap().getString("confKey")).isEqualTo("value");
        assertThat(plugin.getExecutions().get(0).getConfiguration().getMap().getSubObject("listKey").getSubObjectList("objectKey").get(0).getString("field1")).isEqualTo("obj1value1");
        assertThat(plugin.getExecutions().get(0).getConfiguration().getMap().getSubObject("listKey").getSubObjectList("objectKey").get(0).getString("field2")).isEqualTo("obj1value2");
        assertThat(plugin.getExecutions().get(0).getConfiguration().getMap().getSubObject("listKey").getSubObjectList("objectKey").get(0).getString("field3")).isEqualTo("obj1value3");
    }
}
