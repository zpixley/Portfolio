require_relative 'ruby_mine'

class RubyMineTest < Minitest::Test
  def setup
    @rm = RubyMine::new(nil, nil, nil)
  end

  ##  INITIALIZE AND GETTER TESTS  ##

  # This unit test checks that initializing a new RubyMine object assigns values correctly
  def test_initialize
    test_mine = RubyMine.new('test_mine', 1, 1)
    # Assert getters work properly and values are correctly assigned
    assert_equal ['test_mine', 1, 1, [], 0], [test_mine.name, test_mine.max_real, test_mine.max_fake, test_mine.neighbors, test_mine.num_neighbors]
  end

  ##  ADD_NEIGHBOR TESTS  ##

  # This unit test checks that add_neighbor increments num_neighbors
  def test_add_neighbor_increments
    # Make mock neighbor
    dummy_neighbor = Minitest::Mock.new('test_neighbor')
    @rm.add_neighbor(dummy_neighbor)
    # Assert that num_neighbors increments to 1
    assert_equal 1, @rm.num_neighbors
  end

  # This unit test checks that add_neighbor addas an object to neighbors
  # STUB
  def test_add_neighbor_adds
    # Make mock neighbor
    dummy_neighbor = Minitest::Mock.new('test_neighbor')
    def dummy_neighbor.id; 1; end
    @rm.add_neighbor(dummy_neighbor)
    assert_equal 1, @rm.neighbors[0].id
  end
end