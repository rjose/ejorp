ASADMIN=ENV['ASADMIN'] || "asadmin"
NODE_PID="/tmp/node.pid"

desc "Builds the algos jar file"
task :build_algos => FileList["algos/**"] do
  Dir.chdir "algos" do
    sh "rake install"
  end
end

desc "Builds the models jar file"
task :build_models => FileList["models/**"] do
  Dir.chdir "models" do
    sh "rake install"
  end
end

desc "Builds the ejorp war file"
task :build_war => FileList["war/**"] + [:build_algos, :build_models] do
  Dir.chdir "war" do
    sh "rake build"
  end
end

desc "Runs the frontend ejorp node server"
task :run_node => FileList["node/**.js"] do
  Dir.chdir "node" do
    if File.exists? NODE_PID then
      sh "cat " + NODE_PID + " | xargs kill; fi"
    end
    sh "nohup node app.js & echo $! > " + NODE_PID
  end
end

desc "Runs the backend ejorp glassfish server"
task :run_glassfish => [:build_war] do
  CMD=ASADMIN + " list-domains"
  RUNNING=%x[#{CMD}]
  if RUNNING.include? "not" then
    sh ASADMIN + " start-domain"
  end
  sh ASADMIN + " deploy --force war/target/ejorp-1.0-SNAPSHOT.war"
end

task :run => [:run_glassfish, :run_node]
task :default => [:run]
