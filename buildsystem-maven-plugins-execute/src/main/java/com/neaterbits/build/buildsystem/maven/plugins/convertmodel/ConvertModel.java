package com.neaterbits.build.buildsystem.maven.plugins.convertmodel;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.maven.project.MavenProject;
import org.apache.maven.model.Activation;
import org.apache.maven.model.ActivationFile;
import org.apache.maven.model.ActivationOS;
import org.apache.maven.model.ActivationProperty;
import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.ConfigurationContainer;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DeploymentRepository;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryPolicy;
import org.apache.maven.model.Resource;
import org.apache.maven.model.Scm;
import org.apache.maven.model.Site;
import org.apache.maven.model.Model;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.Notifier;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Relocation;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenExclusion;
import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenActivation;
import com.neaterbits.build.buildsystem.maven.project.model.MavenActivationFile;
import com.neaterbits.build.buildsystem.maven.project.model.MavenActivationOS;
import com.neaterbits.build.buildsystem.maven.project.model.MavenActivationProperty;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBaseBuild;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuild;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuildPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenCiManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenCommon;
import com.neaterbits.build.buildsystem.maven.project.model.MavenDependencyManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenDistributionManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenDistributionManagementRelocation;
import com.neaterbits.build.buildsystem.maven.project.model.MavenDistributionManagementRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenDistributionManagementSite;
import com.neaterbits.build.buildsystem.maven.project.model.MavenExecution;
import com.neaterbits.build.buildsystem.maven.project.model.MavenFiles;
import com.neaterbits.build.buildsystem.maven.project.model.MavenIssueManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenMailingList;
import com.neaterbits.build.buildsystem.maven.project.model.MavenNotifier;
import com.neaterbits.build.buildsystem.maven.project.model.MavenOrganization;
import com.neaterbits.build.buildsystem.maven.project.model.MavenParent;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProfile;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReportPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReportSet;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReporting;
import com.neaterbits.build.buildsystem.maven.project.model.MavenResource;
import com.neaterbits.build.buildsystem.maven.project.model.MavenScm;

public class ConvertModel {

    public static MavenProject convertProject(com.neaterbits.build.buildsystem.maven.project.model.MavenProject mavenProject) {

        final MavenProject project = new MavenProject();
        
        project.setModel(convertModel(mavenProject));
        
        return project;
    }
    
    private static Model convertModel(com.neaterbits.build.buildsystem.maven.project.model.MavenProject project) {
        
        final Model model = new Model();
        
        model.setGroupId(project.getModuleId().getGroupId());
        model.setArtifactId(project.getModuleId().getArtifactId());
        model.setVersion(project.getModuleId().getVersion());
        model.setPackaging(project.getPackaging());
        
        final MavenParent mavenParent = project.getParent();

        if (mavenParent != null) {
            
            final Parent parent = new Parent();
            
            parent.setGroupId(mavenParent.getModuleId().getGroupId());
            parent.setArtifactId(mavenParent.getModuleId().getArtifactId());
            parent.setVersion(mavenParent.getModuleId().getVersion());
            parent.setRelativePath(mavenParent.getRelativePath());
            
            model.setParent(parent);
        }
        
        model.setDescription(project.getDescription());
        model.setName(project.getName());
        
        convertCommon(model, project.getCommon());

        if (project.getCommon().getBuild() != null) {
            model.setBuild(convertBuild(project.getCommon().getBuild()));
        }
        
        if (project.getCiManagement() != null) {
            model.setCiManagement(convertCiManagement(project.getCiManagement()));
        }

        if (project.getIssueManagement() != null) {
            model.setIssueManagement(convertIssueManagement(project.getIssueManagement()));
        }
        
        if (project.getMailingLists() != null) {
            model.setMailingLists(project.getMailingLists().stream()
                        .map(ConvertModel::convertMailingList)
                        .collect(Collectors.toUnmodifiableList()));
        }
        
        if (project.getOrganization() != null) {
            model.setOrganization(convertOrganization(project.getOrganization()));
        }
        
        if (project.getProfiles() != null) {
            model.setProfiles(project.getProfiles().stream()
                    .map(ConvertModel::convertProfile)
                    .collect(Collectors.toList()));
        }
        
        if (project.getProperties() != null) {
            
            final Properties properties = new Properties();

            properties.putAll(project.getProperties());
            
            model.setProperties(properties);
        }

        if (project.getScm() != null) {
            model.setScm(convertScm(project.getScm()));
        }
        
        return model;
    }
    
    private static void convertCommon(ModelBase model, MavenCommon common) {
        
        model.setModules(common.getModules());
        
        if (common.getDependencies() != null) {
            model.setDependencies(convertDependencies(common.getDependencies()));
        }
        
        if (common.getDependencyManagement() != null) {
            model.setDependencyManagement(convertDependencyManagement(common.getDependencyManagement()));
        }
        
        if (common.getDistributionManagement() != null) {
            model.setDistributionManagement(convertDistributionManagement(common.getDistributionManagement()));
        }
        
        if (common.getReporting() != null) {
            model.setReporting(convertReporting(common.getReporting()));
        }
        
        if (common.getRepositories() != null) {
            model.setRepositories(convertRepositories(common.getRepositories()));
        }

        if (common.getPluginRepositories() != null) {
            model.setPluginRepositories(convertRepositories(common.getPluginRepositories()));
        }
    }
    
    private static Profile convertProfile(MavenProfile mavenProfile) {
        
        final Profile profile = new Profile();
        
        profile.setId(profile.getId());
        
        if (mavenProfile.getActivation() != null) {
            profile.setActivation(convertActivation(mavenProfile.getActivation()));
        }
        
        convertCommon(profile, mavenProfile.getCommon());
        
        if (mavenProfile.getCommon().getBuild() != null) {
            
            final BuildBase buildBase = new BuildBase();
            
            convertBuildBase(buildBase, mavenProfile.getCommon().getBuild());
            
            profile.setBuild(buildBase);
        }
        
        return profile;
    }
    
    private static Activation convertActivation(MavenActivation mavenActivation) {
     
        final Activation activation = new Activation();
        
        activation.setActiveByDefault(mavenActivation.getActiveByDefault());
        activation.setJdk(mavenActivation.getJdk());
        activation.setFile(convertActivationFile(mavenActivation.getFile()));
        
        if (mavenActivation.getOs() != null) {
            activation.setOs(convertActivationOS(mavenActivation.getOs()));
        }
        
        if (mavenActivation.getProperty() != null) {
            activation.setProperty(convertActivationProperty(mavenActivation.getProperty()));
        }
        
        return activation;
    }
    
    private static ActivationFile convertActivationFile(MavenActivationFile mavenActivationFile) {
        
        final ActivationFile activationFile = new ActivationFile();
        
        activationFile.setExists(mavenActivationFile.getExists());
        activationFile.setMissing(mavenActivationFile.getMissing());
        
        return activationFile;
    }
    
    private static ActivationOS convertActivationOS(MavenActivationOS mavenActivationOS) {
        
        final ActivationOS activationOS = new ActivationOS();

        activationOS.setName(mavenActivationOS.getName());
        activationOS.setFamily(mavenActivationOS.getFamily());
        activationOS.setArch(mavenActivationOS.getArch());
        activationOS.setVersion(mavenActivationOS.getVersion());
        
        return activationOS;
    }
    
    private static ActivationProperty convertActivationProperty(MavenActivationProperty mavenActivationProperty) {
        
        final ActivationProperty activationProperty = new ActivationProperty();
        
        activationProperty.setName(mavenActivationProperty.getName());
        activationProperty.setValue(mavenActivationProperty.getValue());
        
        return activationProperty;
    }
    
    private static Organization convertOrganization(MavenOrganization mavenOrganization) {
        
        final Organization organization = new Organization();
        
        organization.setName(mavenOrganization.getName());
        organization.setUrl(mavenOrganization.getUrl());
        
        return organization;
    }
    
    private static Scm convertScm(MavenScm mavenScm) {
        
        final Scm scm = new Scm();
        
        scm.setConnection(mavenScm.getConnection());
        scm.setDeveloperConnection(mavenScm.getDeveloperConnection());
        scm.setTag(mavenScm.getTag());
        scm.setUrl(mavenScm.getUrl());
        
        return scm;
    }
    
    private static MailingList convertMailingList(MavenMailingList mavenMailingList) {
        
        final MailingList mailingList = new MailingList();
        
        mailingList.setName(mavenMailingList.getName());
        mailingList.setSubscribe(mavenMailingList.getSubscribe());
        mailingList.setUnsubscribe(mavenMailingList.getUnsubscribe());
        mailingList.setPost(mavenMailingList.getPost());
        mailingList.setArchive(mavenMailingList.getArchive());
        mailingList.setOtherArchives(mavenMailingList.getOtherArchives());
        
        return mailingList;
    }
    
    private static DistributionManagement convertDistributionManagement(MavenDistributionManagement mavenDistributionManagement) {
        
        final DistributionManagement distributionManagement = new DistributionManagement();
        
        distributionManagement.setDownloadUrl(mavenDistributionManagement.getDownloadUrl());
        distributionManagement.setStatus(mavenDistributionManagement.getStatus());
        
        if (mavenDistributionManagement.getRepository() != null) {
            distributionManagement.setRepository(
                    convertDistributionManagementRepository(
                            mavenDistributionManagement.getRepository()));
        }
        
        if (mavenDistributionManagement.getSnapshotRepository() != null) {
            distributionManagement.setSnapshotRepository(
                    convertDistributionManagementRepository(
                            mavenDistributionManagement.getSnapshotRepository()));
        }
        
        if (mavenDistributionManagement.getSite() != null) {
            distributionManagement.setSite(convertDistributionManagementSite(mavenDistributionManagement.getSite()));
        }

        if (mavenDistributionManagement.getRelocation() != null) {
            distributionManagement.setRelocation(
                    convertDistributionManagementRelocation(mavenDistributionManagement.getRelocation()));
        }
        
        return distributionManagement;
    }
    
    private static DeploymentRepository convertDistributionManagementRepository(MavenDistributionManagementRepository mavenRepository) {
        
        final DeploymentRepository repository = new DeploymentRepository();
        
        repository.setUniqueVersion(mavenRepository.getUniqueVersion());
        repository.setId(mavenRepository.getId());
        repository.setName(mavenRepository.getName());
        repository.setUrl(mavenRepository.getUrl());
        repository.setLayout(mavenRepository.getLayout());
        
        return repository;
    }
    
    private static IssueManagement convertIssueManagement(MavenIssueManagement mavenIssueManagement) {
        
        final IssueManagement issueManagement = new IssueManagement();
        
        issueManagement.setSystem(mavenIssueManagement.getSystem());
        issueManagement.setUrl(mavenIssueManagement.getUrl());
        
        return issueManagement;
    }
    
    private static Site convertDistributionManagementSite(MavenDistributionManagementSite mavenSite) {
        
        final Site site = new Site();
        
        site.setId(mavenSite.getId());
        site.setName(mavenSite.getName());
        site.setUrl(mavenSite.getName());
        
        return site;
    }
    
    private static Relocation convertDistributionManagementRelocation(MavenDistributionManagementRelocation mavenRelocation) {
        
        final Relocation relocation = new Relocation();
    
        if (mavenRelocation.getModuleId() != null) {
            relocation.setGroupId(mavenRelocation.getModuleId().getGroupId());
            relocation.setArtifactId(mavenRelocation.getModuleId().getArtifactId());
            relocation.setVersion(mavenRelocation.getModuleId().getVersion());
        }
        
        relocation.setMessage(mavenRelocation.getMessage());
        
        return relocation;
    }
    
    private static CiManagement convertCiManagement(MavenCiManagement mavenCiManagement) {
        
        final CiManagement ciManagement = new CiManagement();
        
        ciManagement.setSystem(mavenCiManagement.getSystem());
        ciManagement.setUrl(mavenCiManagement.getUrl());
        
        if (mavenCiManagement.getNotifiers() != null) {

            ciManagement.setNotifiers(mavenCiManagement.getNotifiers().stream()
                    .map(ConvertModel::convertNotifier)
                    .collect(Collectors.toList()));
        }
        
        return ciManagement;
    }
    
    private static Notifier convertNotifier(MavenNotifier mavenNotifier) {
        
        final Notifier notifier = new Notifier();
        
        notifier.setType(mavenNotifier.getType());
        
        if (mavenNotifier.getConfiguration() != null) {
            notifier.setConfiguration(new Properties(mavenNotifier.getConfiguration()));
        }
        
        return notifier;
    }
    
    private static List<Repository> convertRepositories(List<? extends BaseMavenRepository> repositories) {
        
        return repositories.stream()
                .map(ConvertModel::convertRepository)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private static Repository convertRepository(BaseMavenRepository mavenRepository) {
        
        final Repository repository = new Repository();
        
        repository.setId(mavenRepository.getId());
        repository.setName(mavenRepository.getName());
        repository.setUrl(mavenRepository.getUrl());
        repository.setLayout(mavenRepository.getLayout());

        if (mavenRepository.getReleases() != null) {
            repository.setReleases(convertRepositoryPolicy(mavenRepository.getReleases()));
        }

        if (mavenRepository.getSnapshots() != null) {
            repository.setSnapshots(convertRepositoryPolicy(mavenRepository.getSnapshots()));
        }
        
        return repository;
    }
    
    private static RepositoryPolicy convertRepositoryPolicy(MavenFiles mavenFiles) {
        
        final RepositoryPolicy policy = new RepositoryPolicy();
    
        policy.setEnabled(mavenFiles.getEnabled() != null ? mavenFiles.getEnabled() : false);
        policy.setUpdatePolicy(mavenFiles.getUpdatePolicy());
        policy.setChecksumPolicy(mavenFiles.getChecksumPolicy());
        
        return policy;
    }

    private static void convertBuildBase(BuildBase build, MavenBaseBuild mavenBuild) {

        build.setDefaultGoal(mavenBuild.getDefaultGoal());
        build.setDirectory(mavenBuild.getDirectory());
        build.setFinalName(mavenBuild.getFinalName());

        if (mavenBuild.getResources() != null) {
            build.setResources(convertResources(mavenBuild.getResources()));
        }
        
        if (mavenBuild.getTestResources() != null) {
            build.setTestResources(convertResources(mavenBuild.getTestResources()));
        }
        
        if (mavenBuild.getFilters() != null) {
            build.setFilters(mavenBuild.getFilters());
        }

        if (mavenBuild.getPluginManagement() != null) {
            build.setPluginManagement(convertPluginManagement(mavenBuild.getPluginManagement()));
        }
        
        if (mavenBuild.getPlugins() != null) {
            build.setPlugins(convertPlugins(mavenBuild.getPlugins()));
        }
    }
    
    private static Build convertBuild(MavenBuild mavenBuild) {
        
        final Build build = new Build();

        convertBuildBase(build, mavenBuild);
        
        build.setOutputDirectory(mavenBuild.getOutputDirectory());
        build.setSourceDirectory(mavenBuild.getSourceDirectory());
        build.setScriptSourceDirectory(mavenBuild.getScriptSourceDirectory());
        build.setTestSourceDirectory(mavenBuild.getTestSourceDirectory());

        return build;
    }
    
    private static List<Resource> convertResources(List<MavenResource> mavenResources) {
        
        return mavenResources.stream()
                .map(ConvertModel::convertResource)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private static Resource convertResource(MavenResource mavenResource) {
        
        final Resource resource = new Resource();
    
        resource.setDirectory(mavenResource.getDirectory());
        resource.setTargetPath(mavenResource.getTargetPath());
        resource.setFiltering(mavenResource.getFiltering());
        resource.setIncludes(mavenResource.getIncludes());
        resource.setExcludes(mavenResource.getExcludes());
        
        return resource;
    }
    
    private static PluginManagement convertPluginManagement(MavenPluginManagement mavenPluginManagement) {
        
        final PluginManagement pluginManagement = new PluginManagement();
    
        if (mavenPluginManagement.getPlugins() != null) {
            pluginManagement.setPlugins(convertPlugins(mavenPluginManagement.getPlugins()));
        }
        
        return pluginManagement;
    }
    
    private static List<Plugin> convertPlugins(List<MavenBuildPlugin> mavenPlugins) {
        
        return mavenPlugins.stream()
                .map(ConvertModel::convertPlugin)
                .collect(Collectors.toUnmodifiableList());
    }

    private static Plugin convertPlugin(MavenBuildPlugin mavenPlugin) {
        
        final Plugin plugin = new Plugin();
        
        if (mavenPlugin.getModuleId() != null) {
        
            plugin.setGroupId(mavenPlugin.getModuleId().getGroupId());
            plugin.setArtifactId(mavenPlugin.getModuleId().getArtifactId());
            plugin.setVersion(mavenPlugin.getModuleId().getVersion());
        }
        
        if (mavenPlugin.getConfiguration() != null) {
            initConfiguration(plugin, mavenPlugin.getConfiguration());
        }

        plugin.setExtensions(mavenPlugin.getExtensions());

        if (mavenPlugin.getExecutions() != null) {
            plugin.setExecutions(mavenPlugin.getExecutions().stream()
                            .map(ConvertModel::convertExecution)
                            .collect(Collectors.toUnmodifiableList()));
        }
        
        if (mavenPlugin.getDependencies() != null) {
            plugin.setDependencies(convertDependencies(mavenPlugin.getDependencies()));
        }
        
        return plugin;
    }
    
    private static PluginExecution convertExecution(MavenExecution mavenExecution) {

        final PluginExecution execution = new PluginExecution();
        
        if (execution.getConfiguration() != null) {
            initConfiguration(execution, mavenExecution.getConfiguration());
        }
        
        execution.setGoals(mavenExecution.getGoals());
        
        execution.setId(mavenExecution.getId());
        execution.setPhase(mavenExecution.getPhase());
        
        return execution;
    }
    
    private static DependencyManagement convertDependencyManagement(MavenDependencyManagement mavenDependencyManagement) {
        
        final DependencyManagement dependencyManagement = new DependencyManagement();
        
        if (mavenDependencyManagement.getDependencies() != null) {
            dependencyManagement.setDependencies(convertDependencies(mavenDependencyManagement.getDependencies()));
        }
        
        return dependencyManagement;
    }
    
    private static List<Dependency> convertDependencies(List<MavenDependency> dependencies) {
        
        return dependencies.stream()
                .map(ConvertModel::convertDependency)
                .collect(Collectors.toUnmodifiableList());
    }

    private static Dependency convertDependency(MavenDependency mavenDependency) {
        
        final Dependency dependency = new Dependency();
        
        if (mavenDependency.getModuleId() != null) {
            dependency.setGroupId(mavenDependency.getModuleId().getGroupId());
            dependency.setArtifactId(mavenDependency.getModuleId().getArtifactId());
            dependency.setVersion(mavenDependency.getModuleId().getVersion());
        }
        
        dependency.setOptional(mavenDependency.getOptional());
        dependency.setScope(mavenDependency.getScope());
        dependency.setType(mavenDependency.getType());
        dependency.setClassifier(mavenDependency.getClassifier());
        
        if (mavenDependency.getExclusions() != null) {
            dependency.setExclusions(mavenDependency.getExclusions().stream()
                        .map(ConvertModel::convertExclusion)
                        .collect(Collectors.toList()));
        }

        return dependency;
    }
    
    private static Exclusion convertExclusion(MavenExclusion mavenExclusion) {
        
        final Exclusion exclusion = new Exclusion();
    
        exclusion.setGroupId(mavenExclusion.getGroupId());
        exclusion.setArtifactId(mavenExclusion.getArtifactId());
        
        return exclusion;
    }
    
    private static Reporting convertReporting(MavenReporting mavenReporting) {
        
        final Reporting reporting = new Reporting();
        
        reporting.setExcludeDefaults(mavenReporting.getExcludeDefaults());
        reporting.setOutputDirectory(mavenReporting.getOutputDirectory());
        
        if (mavenReporting.getPlugins() != null) {

            reporting.setPlugins(mavenReporting.getPlugins().stream()
                    .map(ConvertModel::convertReportPlugin)
                    .collect(Collectors.toUnmodifiableList()));
            
        }
        
        return reporting;
    }
    
    private static ReportPlugin convertReportPlugin(MavenReportPlugin mavenReportPlugin) {
        
        final ReportPlugin reportPlugin = new ReportPlugin();

        if (mavenReportPlugin.getModuleId() != null) {
            reportPlugin.setGroupId(mavenReportPlugin.getModuleId().getGroupId());
            reportPlugin.setArtifactId(mavenReportPlugin.getModuleId().getArtifactId());
            reportPlugin.setVersion(mavenReportPlugin.getModuleId().getVersion());
        }
        
        if (mavenReportPlugin.getConfiguration() != null ) {
            initConfiguration(reportPlugin, mavenReportPlugin.getConfiguration());
        }
        
        if (mavenReportPlugin.getReportSets() != null ) {
            reportPlugin.setReportSets(mavenReportPlugin.getReportSets().stream()
                                            .map(ConvertModel::convertReportSet)
                                            .collect(Collectors.toUnmodifiableList()));
        }

        return reportPlugin;
    }

    private static ReportSet convertReportSet(MavenReportSet mavenReportSet) {
        
        final ReportSet reportSet = new ReportSet();
        
        reportSet.setId(mavenReportSet.getId());

        if (mavenReportSet.getReports() != null) {
            reportSet.setReports(mavenReportSet.getReports());
        }
        
        if (mavenReportSet.getConfiguration() != null) {
            initConfiguration(reportSet, mavenReportSet.getConfiguration());
        }
        
        return reportSet;
    }
    
    private static void initConfiguration(ConfigurationContainer configurationContainer, MavenConfiguration mavenConfiguration) {

        configurationContainer.setInherited(mavenConfiguration.getInherited());

        // TODO set configuration object
    }
}
