
class Config{
    def jenkinsFolder;
    def scriptPath;
    def repoUrl;
    def module;
    def defaultScriptId;
}
class ConfigFile{
    def id;
    def name;
    def comment;
    def location;
}
def repoUrl = 'https://github.com/devonfw-forge/mrchecker-source.git'
def modules = ['mrchecker-core-module','mrchecker-database-module','mrchecker-example-module','mrchecker-mobile-module','mrchecker-security-module','mrchecker-selenium-module','mrchecker-webapi-module','mrchecker-cli-module']
def configs = []
def folders = ['build','test','deploy']
def branchesWithView = ['develop','feature/new_CICD_process']
modules.each{
    configs << new Config(jenkinsFolder:'build', scriptPath:'CICD/Build_Jenkinsfile', repoUrl: repoUrl, module:it,defaultScriptId:'buildDispatcher')
    configs << new Config(jenkinsFolder:'test' , scriptPath:"mrchecker-framework-modules/${it}/Jenkinsfile", repoUrl: repoUrl, module:it, defaultScriptId:'testDispatcher')
    configs << new Config(jenkinsFolder:'deploy', scriptPath:'CICD/Deploy_Jenkinsfile', repoUrl: repoUrl, module:it, defaultScriptId:'deployDispatcher')
}


configFiles = []
configFiles << new ConfigFile(id:'buildDispatcher',name:'buildDispatcher',comment:'script for providing default fallback if no pipelinescript found in repo',location:'CICD/BuildDispatcher.groovy')
configFiles << new ConfigFile(id:'testDispatcher',name:'testDispatcher',comment:'script for providing default fallback if no pipelinescript found in repo',location:'CICD/TestDispatcher.groovy')
configFiles << new ConfigFile(id:'deployDispatcher',name:'deployDispatcher',comment:'script for providing default fallback if no pipelinescript found in repo',location:'CICD/DeployDispatcher.groovy')

configFiles << new ConfigFile(id:'buildDefault',name:'buildDefault',comment:'default script for BUILD stage',location:'CICD/Build_Jenkinsfile')
configFiles << new ConfigFile(id:'testDefault',name:'testDefault',comment:'default script for TEST stage',location:'CICD/Test_Jenkinsfile')
configFiles << new ConfigFile(id:'deployDefault',name:'deployDefault',comment:'default script for DEPLOY stage',location:'CICD/Deploy_Jenkinsfile')

configFiles << new ConfigFile(id:'deployParentJenkinsfile',name:'deployParentJenkinsfile',comment:'default script for DEPLOY stage for parent pom',location:'CICD/Deploy_Parent_Jenkinsfile')

    def script = makeJobs(configs,folders)
    //script += makeViews(modules,branchesWithView)
    script += makeConfigFiles(configFiles)
    script += addJobForDeployParent()
print '\n'*5+'-'*80+'\n\t\t\tSCRIPT\n'
print script
print '\n'*5+'-'*80+'\n\t\t\tEND\n'
node{
    jobDsl scriptText: script
}

def addJobForDeployParent(){
    return """
    multibranchPipelineJob("deploy/mrchecker-test-framework"){
        description("Build source code and provide packages")
        branchSources{
            branchSource{
                source{
                    git{
                        id('12314')
                        remote('https://github.com/devonfw-forge/mrchecker-source.git')
                        traits{
                            gitBranchDiscovery()
                        }
                    }
                }
                strategy{
                    defaultBranchPropertyStrategy {
                        props{
                            noTriggerBranchProperty()
                        }
                    }
                }
            }
        }
        orphanedItemStrategy{
            discardOldItems{
                daysToKeep(30)
            }
        }
        factory{
            pipelineBranchDefaultsProjectFactory {
                scriptId('deployParentJenkinsfile')
                useSandbox(false)
            }
        }
        triggers{
            cron("")
        }
    }
    """
}
def makeConfigFiles(cfgFiles){
    def script = ""
    node('master'){
        checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: '*/feature/new_CICD_process']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/devonfw-forge/mrchecker-source.git']]]
        cfgFiles.each{f->
            def content = readFile(file:f.location,encoding:'UTF-8')
            script+=$/
                    configFiles{groovyScript{
                        id('${f.id}')
                        name('${f.name}')
                        comment('${f.comment}')
                        content('''${content}''')
                    }}/$
        }

    }
    return script
}

@NonCPS
def makeViews(modules,branches){
    //build/mrchecker-core-module/feature%2Fnew_CICD_process
    def script = ""
    modules.each{ m ->
        script+="nestedView('${m}') { \n views {"
        branches.each{ b ->
            script+="""
                listView('${b}'){
                    jobs{
                        regex('(?:build|deploy|test)/${m}/${b}')
                    }
                    columns {
                        status()
                        weather()
                        name()
                        lastSuccess()
                        lastFailure()
                    }
                }
            """
        }
        script+="}}"
    }
    return script
}
@NonCPS
def makeJobs(configs,folders){
    def jobTemplateText = """
    multibranchPipelineJob("\${it.jenkinsFolder}/\${it.module}"){
        description("Build source code and provide packages")
        branchSources{
            branchSource{
                source{
                    git{
                        id('12314')
                        remote('\${it.repoUrl}')
                        traits{
                            gitBranchDiscovery()
                        }
                    }
                }
                strategy{
                    defaultBranchPropertyStrategy {
                        props{
                            noTriggerBranchProperty()
                        }
                    }
                }
            }
        }
        orphanedItemStrategy{
            discardOldItems{
                daysToKeep(30)
            }
        }
        factory{
            pipelineBranchDefaultsProjectFactory {
                scriptId('\${it.defaultScriptId}')
                useSandbox(false)
            }
        }
        triggers{
            cron("")
        }
    }
    """
    def script = ''
    def engine = new groovy.text.SimpleTemplateEngine()
    def jobTemplate = engine.createTemplate(jobTemplateText)
    folders.each{
        script += "folder('${it}')\n"
    }
    configs.each{
        script += jobTemplate.make([it:it])
    }
    return script
}
