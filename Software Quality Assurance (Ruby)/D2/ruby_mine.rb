#  RubyMine is a class that holds information for each mine
class RubyMine
  attr_reader :name, :max_real, :max_fake, :neighbors, :num_neighbors

  def initialize(name, max_real, max_fake)
    @name = name
    @max_real = max_real
    @max_fake = max_fake
    @neighbors = []
    @num_neighbors = 0
  end

  def add_neighbor(neighbor)
    @neighbors.push(neighbor)
    @num_neighbors = @num_neighbors.to_i + 1
  end
end
