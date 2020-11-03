require_relative 'game.rb'

args = ARGV

# checks input has 3 integer arguments and and that the num_prospectors and num_turns values are non-negative
if (args.length != 3) || (args[1].to_i < 0 || args[2].to_i < 0) ||
   (args[0].to_i.to_s != args[0] || args[1].to_i.to_s != args[1] || args[2].to_i.to_s != args[2])
  puts "Usage:
        ruby ruby_rush.rb *seed* *num_prospectors* *num_turns*
        *seed* should be an integer
        *num_prospectors* should be a non-negative integer
        *num_turns* should be a non-negative integer\n"
  exit 1
end

game = Game.new(args[0].to_i, args[1].to_i, args[2].to_i)
game.next_prospector while game.done != 1
exit 0
