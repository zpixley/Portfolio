require 'flamegraph'
require 'set'
require_relative 'block.rb'
require_relative 'wallet.rb'

# Class Helper houses all of the functionality needed to verify a valid blockchain of billcoins
class Helper
  attr_accessor :wallets, :negs, :actives, :p_s, :p_ns, :p_h, :curr_str, :curr_line, :curr_wallet, :curr_hash

  def calc_hash(str)
    s = str.unpack('U*')
    h = 0

    # sum hashed value of each character in string
    s.each do |x|
      h += ((x**3_000) + (x**x) - (3**x)) * (7**x)
    end

    # resolve final hash of string
    h = h % 65_536
    # convert hash to hex
    h = h.to_s(16)
    i = 0

    # remove leading zeroes
    h = h[++i, 3] while !h[i].nil? && h[i].chr == '0' && h[i].length > 1

    h.to_s
  end

  # def calc_hash(str)
  #  s = str.unpack('U*')
  #  h = 0
  #  i = 0
  #
  #  s.each do |x|
  #    arg1 = Thread.new { x**3_000 }
  #    arg2 = Thread.new { x**x }
  #    arg3 = Thread.new { 3**x }
  #    arg4 = Thread.new { 7**x }
  #
  #    [arg1, arg2, arg3, arg4].each { |a| a.join }
  #
  #    h += (arg1.value + arg2.value - arg3.value) * arg4.value
  #  end
  #
  #  h = (h % 65_536).to_s(16)
  #
  #  h = h[++i, 3] while !h[i].nil? && h[i].chr == '0' && h[i].length > 1
  #
  #  h.to_s
  # end

  def check_format
    parts = @curr_str.split('|')
    return 1 unless @curr_str.count('|') == 4 && parts.length == 5

    parts.each { |p| return 1 if p.nil? }

    prev_hash = parts[1]
    transactions = parts[2].split(':')
    timestamp = parts[3]
    next_hash = parts[4]

    # unable to parse transactions list
    return 3 unless transactions.length - parts[2].count(':') == 1

    transactions.each do |t|
      return 3 unless t.count('>') == 1 && t.count('(') == 1 && t.count(')') == 1
    end

    # unable to parse timestamp
    return 5 unless timestamp.count('.') == 1

    # invalid hash length
    return 9 unless prev_hash.length <= 4 && next_hash.length <= 4

    0
  end

  def parse_block
    # parse error (1)
    code = check_format

    parts = @curr_str.split('|')

    return Block.new(@curr_str, nil, nil, nil, nil, nil, nil, code) unless code.zero?

    number = parts[0].to_i
    prev_hash = parts[1]
    transactions = parts[2].split(':')
    timestamp = parts[3].split('.')
    seconds = timestamp[0].to_i
    nseconds = timestamp[1].to_i
    next_hash = parts[4]

    # invalid block number (2)
    if number != @curr_line
      code = 2
    # invalid prev hash (8)
    elsif !number.zero? && prev_hash != @p_h
      code = 8
    end

    # invalid timestamp (6)
    if @p_s != 0 && seconds <= @p_s && nseconds < @p_ns
      code = 6
    # bad block hash (7)
    elsif next_hash != @curr_hash # calc_hash(parts[0] + '|' + prev_hash + '|' + parts[2] + '|' + parts[3])
      code = 7
    end

    block = Block.new(@curr_str, number, prev_hash, transactions, seconds, nseconds, next_hash, code)

    block
  end

  # Encoded error types
  # 1: parse error
  # 2: invalid block number
  # 3: parse error (transactions)
  # 4: invalid balance error
  # 5: parse error (timestamp)
  # 6: invalid timestamp
  # 7: bad block hash
  # 8: bad previous hash
  # 9: hash length > 4
  def print_error
    parts = @curr_blk.text.split('|')
    out = "Line #{@curr_line}:"

    case @curr_blk.validity
    when 1
      out += " Could not parse line '#{@curr_blk.text}."
    when 2
      out += " Invalid block number #{@curr_blk.number}, should be #{@curr_line}."
    when 3
      out += " Could not parse transactions list '#{@curr_blk.text.split('|')[2]}'."
    when 4
      out += " Invalid block, address #{@curr_wallet.address} has #{@curr_wallet.balance} billcoins!"
    when 5
      out += " Could not parse timestamp '#{@curr_blk.text.split('|')[3]}'."
    when 6
      out += " Previous timestamp #{@p_s}.#{@p_ns} >= new timestamp #{@curr_blk.seconds}.#{@curr_blk.nseconds}."
    when 7
      h_string = @curr_blk.text.chomp('|' + parts[4])
      out += " String '#{h_string}' hash set to #{parts[4]}, should be #{@curr_hash}."
    when 8
      out += "Previous hash was #{parts[1]}, should be #{@p_h}."
    when 9
      out += " Previous hash was #{parts[1]}, should be 4 characters or less." if parts[1].length > 4
      out += " Block hash was #{parts[4]}, should be 4 characters or less." if parts[4].length > 4
    end

    puts out

    @curr_blk.validity
  end

  # return if transaction cannot be parsed
  def complete_transaction(trans)
    trans = trans.split('>')

    return 1 if trans.size != 2

    s = trans[0]
    r = trans[1].split('(')[0]
    x = trans[1].split('(')[1].chomp(')')

    return 1 unless s.length == r.length && s.length == 6 && x.to_i.to_s == x

    sender = @wallets[s.to_i]
    recipient = @wallets[r.to_i]
    x = x.to_i

    # update sender wallet
    if @wallets[s.to_i].nil?
      @wallets[s.to_i] = Wallet.new(s, x * -1)
    else
      sender.balance -= x
      @negs.push(sender) if sender.address != 'SYSTEM' && sender.balance.negative?
    end

    # update recipients wallet
    if @wallets[r.to_i].nil?
      @wallets[r.to_i] = Wallet.new(r, x)
    else
      recipient.balance += x
    end

    @actives.add(s)
    @actives.add(r)
  end

  def verify_blockchain(blkch)
    @wallets = Array.new(1_000_000)
    @actives = Set[]
    @curr_blk = Block.new('0|0|000000:000000(0)|0.0|0', 0, 0, 0, 0, 0, 0, 0)

    i = 0

    blkch.each do |b|
      @p_s = @curr_blk.seconds
      @p_ns = @curr_blk.nseconds
      @p_h = @curr_blk.next_hash
      @negs = []
      @curr_str = b
      @curr_line = i
      @curr_hash = calc_hash(@curr_str.chomp('|' + @curr_str.split('|')[4].to_s))
      @curr_blk = parse_block

      return print_error if @curr_blk.validity != 0

      @curr_blk.transactions.each do |t|
        if complete_transaction(t) == 1
          @curr_blk.validity = 3
          return print_error
        end
      end

      # OPTIMIZE: checking only the senders balances
      if @negs.length.positive?
        @negs.each do |w|
          @curr_wallet = w
          if @curr_wallet.balance.negative?
            @curr_blk.validity = 4
            return print_error
          end
        end
      end

      i += 1
    end

    0
  end
end
