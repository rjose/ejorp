set :stages, %w(production staging)
set :default_stage, "staging"
require 'capistrano/ext/multistage'

set :application, "ejorp"
set :user, "ec2-user"
set :repository,  "git@github.com:rjose/ejorp.git"
set :use_sudo, false

set :scm, :git
set :deploy_via, :copy
set :copy_strategy, :export
set :copy_cache, true
set :copy_exclude, [".git/*", "algos", "models", "pom.xml", "war", "html5", "tools"]

set :deploy_subdir, "node"

set :deploy_to, "/var/www/#{application}"
set :deploy_env, 'production'

task :uname do
  puts strategy
  run "uname -a"
end
