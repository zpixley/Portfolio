require_relative 'ruby_mine.rb'
require_relative 'prospector.rb'

#  Game houses all functionality of the Ruby Rush simulation
class Game
  attr_reader :seed, :curr_prospector, :num_prospectors, :prospectors, :ruby_mines, :num_turns, :done

  def initialize(seed, num_prospectors, num_turns)
    @seed = seed
    srand @seed.to_i
    @num_prospectors = num_prospectors
    @prospectors = []
    @curr_prospector = Prospector.new(0, 0, 0)
    @ruby_mines = {
      'Enumerable Canyon' => RubyMine.new('Enumerable Canyon', 1, 1),
      'Duck Type Beach' => RubyMine.new('Duck Type Beach', 2, 2),
      'Monkey Patch City' => RubyMine.new('Monkey Patch City', 1, 1),
      'Nil Town' => RubyMine.new('Nil Town', 0, 3),
      'Matzburg' => RubyMine.new('Matzburg', 3, 0),
      'Hash Crossing' => RubyMine.new('Hash Crossing', 2, 2),
      'Dynamic Palisades' => RubyMine.new('Dynamic Palisades', 2, 2)
    }
    @num_turns = num_turns
    @done = 0
    add_prospectors
    connect_mines
  end

  # connect_mines adds all the neighbor to the correct RubyMine objects as specified in the requirements
  def connect_mines
    ruby_mines['Enumerable Canyon'].add_neighbor(ruby_mines['Duck Type Beach'])
    ruby_mines['Enumerable Canyon'].add_neighbor(ruby_mines['Monkey Patch City'])
    ruby_mines['Duck Type Beach'].add_neighbor(ruby_mines['Enumerable Canyon'])
    ruby_mines['Duck Type Beach'].add_neighbor(ruby_mines['Matzburg'])
    ruby_mines['Monkey Patch City'].add_neighbor(ruby_mines['Nil Town'])
    ruby_mines['Monkey Patch City'].add_neighbor(ruby_mines['Enumerable Canyon'])
    ruby_mines['Monkey Patch City'].add_neighbor(ruby_mines['Matzburg'])
    ruby_mines['Nil Town'].add_neighbor(ruby_mines['Monkey Patch City'])
    ruby_mines['Nil Town'].add_neighbor(ruby_mines['Hash Crossing'])
    ruby_mines['Matzburg'].add_neighbor(ruby_mines['Monkey Patch City'])
    ruby_mines['Matzburg'].add_neighbor(ruby_mines['Duck Type Beach'])
    ruby_mines['Matzburg'].add_neighbor(ruby_mines['Hash Crossing'])
    ruby_mines['Matzburg'].add_neighbor(ruby_mines['Dynamic Palisades'])
    ruby_mines['Hash Crossing'].add_neighbor(ruby_mines['Matzburg'])
    ruby_mines['Hash Crossing'].add_neighbor(ruby_mines['Nil Town'])
    ruby_mines['Hash Crossing'].add_neighbor(ruby_mines['Dynamic Palisades'])
    ruby_mines['Dynamic Palisades'].add_neighbor(ruby_mines['Matzburg'])
    ruby_mines['Dynamic Palisades'].add_neighbor(ruby_mines['Hash Crossing'])
  end

  def add_prospectors
    for i in 0..num_prospectors - 1
      @prospectors.push(Prospector.new(i + 1, @ruby_mines['Enumerable Canyon'], @num_turns))
    end
    @curr_prospector = @prospectors[0]
  end

  def next_prospector
    puts "Rubyist ##{@curr_prospector.num} starting in Enumerable Canyon.\n"
    while @curr_prospector.turns_left > 0
      new_real, new_fake = @curr_prospector.mine
      print_day(@curr_prospector.loc, new_real, new_fake)
      @curr_prospector.next_day(new_real, new_fake)
    end

    @curr_prospector.go_home

    if @curr_prospector.num == num_prospectors
      @done = 1
    else
      @curr_prospector = @prospectors[@curr_prospector.num]
    end
  end

  def print_day(loc, new_real, new_fake)
    if new_real < 0 || new_fake < 0
      puts 'Error: new_real or new_fake is negative in print_day()'
      return -1
    end

    # Control structure to find correct print statement for input
    if new_real.to_i.zero?
      if new_fake.to_i.zero?
        puts "\tFound no rubies or fake rubies in #{loc.name}.\n"
      elsif new_fake.to_i == 1
        puts "\tFound 1 fake ruby in #{loc.name}.\n"
      else
        puts "\tFound #{new_fake} fake rubies in #{loc.name}.\n"
      end
    elsif new_real.to_i == 1
      if new_fake.to_i.zero?
        puts "\tFound 1 ruby in #{loc.name}.\n"
      elsif new_fake.to_i == 1
        puts "\tFound 1 ruby and 1 fake ruby in #{loc.name}\n"
      else
        puts "\tFound 1 ruby and #{new_fake} fake rubies in #{loc.name}\n"
      end
    elsif new_fake.to_i.zero?
      puts "\tFound #{new_real} rubies in #{loc.name}\n"
    elsif new_fake.to_i == 1
      puts "\tFound #{new_real} rubies and 1 fake ruby in #{loc.name}\n"
    else
      puts "\tFound #{new_real} rubies and #{new_fake} fake rubies in #{loc.name}\n"
    end
  end
end
