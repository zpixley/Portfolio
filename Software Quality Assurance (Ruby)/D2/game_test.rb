=begin
require 'simplecov'
SimpleCov.start

require 'minitest/autorun'
=end
require_relative 'game'

class GameTest < MiniTest::Test
	def setup
		@g = Game::new(1, 1, 1)
	end

  # UNIT TESTS FOR METHOD initialize(seed, num_prospectors, num_turns)

  # This unit test checks that initializing a new Game object assigns values correctly
  def test_initialize
    rm = [@g.ruby_mines['Enumerable Canyon'].name, @g.ruby_mines['Monkey Patch City'].name, @g.ruby_mines['Nil Town'].name, @g.ruby_mines['Matzburg'].name, @g.ruby_mines['Hash Crossing'].name, @g.ruby_mines['Dynamic Palisades'].name]
    assert_equal [1, 1, ['Enumerable Canyon', 'Monkey Patch City', 'Nil Town', 'Matzburg', 'Hash Crossing', 'Dynamic Palisades'], 1, 0], [@g.seed, @g.num_prospectors, rm, @g.num_turns, @g.done]
  end

  # UNIT TESTS FOR METHOD connect_mines
  # No equivalence partitions necessary as there is no input and the output is the same each use

  # This unit test checks that RubyMine objects are assigned neighbors that match the program requirements
  def test_connect_mines
    @g.connect_mines

    assert [@g.ruby_mines['Enumerable Canyon'].neighbors[0]], ['Duck Type Beach']
    assert [@g.ruby_mines['Enumerable Canyon'].neighbors[1]], ['Monkey Patch City']
  end

  # UNIT TESTS FOR METHOD add_prospectors
  #

  # UNIT TESTS FOR METHOD next_prospector

  # UNIT TESTS FOR METHOD print_day(loc, new_real, new_fake)
  # Equivalence classes:
  # 1a. (new_real = 0 && new_fake = 0)                                      -> first output line: "Found no rubies or fake rubies in #{loc.name}."
  # 1b. (new_real = 0 && new_fake = 1)                                      -> first output line: "Found 1 fake ruby in #{loc.name}."
  # 1c. (new_real = 0 && new_fake != 0 && new_fake != 1)                    -> second output line: "Found #{new_fake} fake rubies in #{loc.name}."
  # 2a. (num_real = 1 && new_fake = 0)                                      -> second output line: "Found 1 ruby in #{loc.name}."
  # 2b. (num_real = 1 && new_fake = 1)                                      -> third output line: "Found 1 ruby and 1 fake ruby in #{loc.name}"
  # 2c. (num_real = 1 && new_fake != 0 && new_fake != 1)                    -> third output line: "Found 1 ruby and #{new_fake} fake rubies in #{loc.name}"
  # 4a. (num_real != 0 && num_real != 1 && num_fake = 0)                    -> fourth output line: "Found #{new_real} rubies in #{loc.name}"
  # 4b. (num_real != 0 && num_real != 1 && num_fake = 1)                    -> fourth output line: "Found #{new_real} rubies and 1 fake ruby in #{loc.name}"
  # 4c. (num_real != 0 && num_real != 1 && num_fake != 0 && num_fake != 1)  -> fourth output line: "Found #{new_real} rubies and #{new_fake} fake rubies in #{loc.name}"

  # This unit test checks outputs a check for combinations of all identified equivalence classes
  # STUB
  def test_print_day
    vals = [0, 1, 5]
    puts "TEST_PRINT_DAY EQUIVALENCE CLASSES\n"
    puts "------------------------------------------------------------\n"
    # Iterate through all identified equivalence classes
    for i in 0..2
      for j in 0..2
        dummy_loc = MiniTest::Mock.new('test_loc')
        def dummy_loc.name; 'Test Location'; end
        puts "location: #{dummy_loc.name}, new_real: #{vals[i]}, new_fake: #{vals[j]}\n"
        @g.print_day(dummy_loc, vals[i], vals[j])
        puts "------------------------------------------------------------\n"
      end
    end
  end

  # This unit test checks what happens if we call print_day with negative values for new_real and new_fake paramters and
  # It should return -1 in that case
  # EDGE CASE
  def test_initialize_edge

    dummy_loc = MiniTest::Mock.new('test_loc')
    #dummy_game = MiniTest::Mock.new('test_game')
    def dummy_loc.name; 'Test Location'; end
    #def dummy_game.print; print_day; end
    g_test = Game.new(dummy_loc, -2, -5)
    # Assert that print_day catches and reports edge case
    assert_equal -1, g_test.print_day(dummy_loc, -2, -5)
  end
end
