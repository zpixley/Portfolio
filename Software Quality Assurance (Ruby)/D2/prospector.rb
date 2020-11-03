require_relative 'ruby_mine'

# Prospector houses all functionality of a simulated prospector in the game
class Prospector
  attr_accessor :num, :loc, :num_real, :num_fake, :num_days, :turns_left

  def initialize(num, loc, max_turns)
    @num = num
    @loc = loc
    @num_real = 0
    @num_fake = 0
    @num_days = 0
    @turns_left = max_turns
  end

  def mine
    # Pseudorandomly generate values for new_real and new_fake
    new_real = rand(0..@loc.max_real.to_i)
    new_fake = rand(0..@loc.max_fake.to_i)
    @num_real = @num_real.to_i + new_real.to_i
    @num_fake = @num_fake.to_i + new_fake.to_i
    # Return generated values
    [new_real, new_fake]
  end

  # next_day chooses Prospector's next day choice based on the previous day's finds
  # returns 1 if prospector changes locations, returns 2 if prospector is out of turns
  def next_day(new_real, new_fake)
    @num_days = @num_days.to_i + 1
    # Turn used when new_real = new_fake = 0
    return unless new_real.to_i.zero? && new_fake.to_i.zero?

    @turns_left = @turns_left.to_i - 1
    # Change locations if prospector has at least one turn left
    if @turns_left > 0
      next_loc
      return 1
    end
    2
  end

  # next_loc updates loc with a psuedorandomly chosen neighbor
  def next_loc
    old_loc = @loc
    # Pseudorandomly generate index to pull from neighbors array
    choice = rand(0..@loc.num_neighbors - 1)
    new_loc = @loc.neighbors[choice]
    @loc = new_loc
    puts "Heading from #{old_loc.name} to #{new_loc.name}.\n"
    # Return choice for testing purposes
    choice
  end

  # go_home prints the correct report of the Prospector's finds, with control structures to use correct grammar
  def go_home
    if @num_days != 1
      puts "After #{@num_days} days, Rubyist #{@num} found:\n"
    else
      puts "After 1 day, Rubyist #{@num} found:\n"
    end

    if @num_real != 1
      puts "\t#{@num_real} rubies.\n"
    else
      puts "\t1 ruby.\n"
    end

    if @num_fake != 1
      puts "\t#{@num_fake} fake rubies.\n"
    else
      puts "\t1 fake ruby.\n"
    end

    if @num_real >= 10
      puts "Going home victorious!\n"
    elsif @num_real > 0
      puts "Going home sad.\n"
    else
      puts "Going home empty-handed.\n"
    end
  end
end
