require_relative 'prospector'

class ProspectorTest < Minitest::Test
	def setup
		@p = Prospector::new(1, nil, 1)
	end

	# UNIT TESTS FOR METHOD initialize(num, loc, num_real, num_fake, num_days, max_turns)
	# It does not make sense to partition this method because it's only function is to assign parameters directly to variables


	# This unit test checks that initializing a new Prospector object assigns 
	# values correctly
	def test_initialize
		# Assert getters work properly and values are correctly assigned
		assert_equal [1, nil, 0, 0, 0, 1], [@p.num, @p.loc, @p.num_real, @p.num_fake, @p.num_days, @p.turns_left]
	end

	# UNIT TESTS FOR METHOD mine

	# This unit test checks that the mine method returns values between zero and the given maximum
  # STUB
	def test_random_ruby_find_bounds
		# Iterate 100 time to test for consistency
		for i in 0..99
			# Dummy RubyMine
			dummy_loc = Minitest::Mock.new('test_loc')
			pros = Prospector.new(1, dummy_loc, 1)
			# Stub methods to mimic max_real and max_fake methods called in mine method
			def dummy_loc.max_real; 10; end
			def dummy_loc.max_fake; 5; end
			# Assert that values are within the expected range and output message in case of error
			out = pros.mine
			assert((out[0] <= 10) && (out[0] >= 0), "error bounds num_real: #{out[0]}")
			assert((out[1] <= 5) && (out[1] >= 0), "error bounds num_fake: #{out[1]}")
		end
	end

	# This unit test checks that num_real and num_fake are updated correctly in mine method
  # STUB
	def test_mine_adds
		# Dummy RubyMine
		dummy_loc = Minitest::Mock.new('test_loc')
		# Stub methods to mimic max_real and max_fake methods called in mine method
		def dummy_loc.max_real; 5; end
		def dummy_loc.max_fake; 4; end
		
		num_real_prev = 0
		num_fake_prev = 0
		num_real_new = 0
		num_fake_new = 0
		pros = Prospector.new(1, dummy_loc, 1)
		# Iterate  10 times to test for consistency
		for i in 0..9
			# Store previous num_real and num_fake to use in assertion
			num_real_prev = pros.num_real
			num_fake_prev = pros.num_fake
			out = pros.mine
			num_real_new = pros.num_real
			num_fake_new = pros.num_fake
			# Assert that each num_real and num_fake are added and updated properly each iteration
			assert(num_real_new = num_real_prev + out[0], "error incrementing num_real (#{pros.num_real} /= #{num_real_prev} + #{out[0]})")
			assert(num_fake_new = num_fake_prev + out[1], "error incrementing num_fake (#{pros.num_fake} /= #{num_fake_prev} + #{out[1]})")
		end
	end

  # UNIT TESTS FOR METHOD next_day(new_real, new_fake)
  # Equivalence classes:
  # 1. (new_real = 0 && new_fake = 0) -> num_turns decrements, next_loc method is called
  # 2. (new_real != 0 || new_fake != 0) -> num_turns does not decrement

  # This unit test checks that num_days increments when next_day method is called
  def test_next_day_increments
    num_days_prev = @p.num_days
    # Testing all equivalence classes and asserting that num_days increments
    @p.next_day(0, 0)
    assert_equal num_days_prev + 1, @p.num_days
    num_days_prev = @p.num_days
    @p.next_day(0, 1)
    assert_equal num_days_prev + 1, @p.num_days
    num_days_prev = @p.num_days
    @p.next_day(1, 0)
    assert_equal num_days_prev + 1, @p.num_days
    num_days_prev = @p.num_days
    @p.next_day(1, 1)
    assert_equal num_days_prev + 1, @p.num_days
  end

  # This unit test checks that num_turns decrements when the next_day method is called with zero values
  def test_next_day_zeroes
    turns_left_prev = @p.turns_left
    # Testing equivalence case 1, asserting that turns_left decrements
    @p.next_day(0, 0)
    assert_equal turns_left_prev - 1, @p.turns_left
  end

  # This unit test checks that num_turns does not decrement when the next_day method is called with at
  # least one non-zero value
  def test_next_day_not_zeroes
    turns_left_prev = @p.turns_left
    # Testing equivalence case 2, asserting that turns_left does not decrement
    @p.next_day(1, 0)
    assert_equal turns_left_prev, @p.turns_left
    @p.next_day(0, 1)
    assert_equal turns_left_prev, @p.turns_left
    @p.next_day(0, 1)
    assert_equal turns_left_prev, @p.turns_left
    @p.next_day(1, 1)
    assert_equal turns_left_prev, @p.turns_left
  end

  # Equivalence partition classes under (new_real = 0 && new_fake = 0):
  # 1. (turns_left <= 0) -> returns 2
  # 2. (turns_left > 0)  -> returns 1

  # This unit test checks that next_loc method is not called when turns_left <= 0
  def test_next_day_internal_no_call
    test_prospector = Prospector.new(1, nil, 1)
    test_prospector.turns_left = 0
    assert_equal test_prospector.next_day(0, 0), 2
  end

  # This unit test checks that next_loc method is  called when turns_left > 0
  # STUB
  def test_next_day_internal_call
    test_prospector = Prospector.new(1, nil, 1)
    def test_prospector.next_loc; 0; end
    test_prospector.turns_left = 2
    assert_equal test_prospector.next_day(0, 0), 1
  end

  # UNIT TESTS FOR METHOD go_home
  # Equivalence classes:
  # 1a. (num_days != 1)     -> first output line: "After #{num_days} days, Rubyist #{num} found:"
  # 1b. (num_days = 1)      -> first output line: "After 1 day, Rubyist #{num} found:"
  # 2a. (num_real != 1)     -> second output line: "#{@num_real} rubies."
  # 2b. (num_real = 1)      -> second output line: "1 ruby."
  # 3a. (num_fake != 1)     -> third output line: "#{@num_fake} fake rubies."
  # 3b. (num_fake = 1)      -> third output line: "1 fake ruby."
  # 4a. (num_real >= 10)    -> fourth output line: "Going home victorious!"
  # 4b. (0 < num_real < 10) -> fourth output line: "Going home sad."
  # 4c. (num_real = 0)      -> fourth output line: "Going home empty-handed."

  # This unit test checks outputs a check for combinations of all identified equivalence classes
  def test_go_home
    num_days_vals = [5, 1]
    num_real_vals = [15, 5, 0, 1]
    num_fake_vals = [6, 1]
    puts "TEST_GO_HOME EQUIVALENCE CLASSES\n"
    puts "------------------------------------------------------------\n"
    for i in 0..1
      for j in 0..3
        for k in 0..1
          @p.num_days = num_days_vals[i]
          @p.num_real = num_real_vals[j]
          @p.num_fake = num_fake_vals[k]
          puts "num_days: #{@p.num_days}, num_real: #{@p.num_real}, num_fake: #{@p.num_fake}\n"
          @p.go_home
          puts "------------------------------------------------------------\n"
        end
      end
    end
  end

end