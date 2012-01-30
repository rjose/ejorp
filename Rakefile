ASADMIN=ENV['ASADMIN'] || "asadmin"
NODE_PID="/tmp/node.pid"

task :build_war => FileList["war/**"] do
  Dir.chdir "war" do
    sh "rake build"
  end
end

task :run_node => FileList["node/**.js"] do
  Dir.chdir "node" do
    if File.exists? NODE_PID then
      sh "cat " + NODE_PID + " | xargs kill; fi"
    end
    sh "nohup node app.js & echo $! > " + NODE_PID
  end
end

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
