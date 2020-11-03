require 'simplecov'

SimpleCov.start do
  add_filter "/game_test.rb"
  add_filter "/prospector_test.rb"
  add_filter "/ruby_mine_test.rb"
end

require 'minitest/autorun'

require_relative 'game_test'
require_relative 'prospector_test'
require_relative 'ruby_mine_test'
