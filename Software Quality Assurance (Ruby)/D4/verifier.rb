require_relative 'helper.rb'

Flamegraph.generate('verifier_flamegraph.html') do
  if ARGV.length != 1 || !File.file?(ARGV[0].to_s)
    puts "\nUsage: ruby verifier.rb <name_of_file>\n\tname_of_file = name of file to verify (make sure file exists)\n\n"
    exit 1
  end

  helper = Helper.new

  if helper.verify_blockchain(File.read(ARGV[0].to_s).split("\n")) != 0 then puts "BLOCKCHAIN INVALID\n\n"
  else
    helper.actives.to_a.sort.each do |a|
      w = helper.wallets[a.to_i]
      puts "#{a}: #{w.balance} billcoins\n" if w.address != 'SYSTEM' && w.balance != 0
    end
    puts "\n"
  end
end
