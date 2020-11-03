# Class Block is on Object to represent an individual block in the blockchain
class Block
  attr_accessor :text, :number, :prev_hash, :transactions, :seconds, :nseconds, :next_hash, :validity

  def initialize(text, num, p_h, trans, sec, nsec, n_h, val)
    @text = text
    @number = num
    @prev_hash = p_h
    @transactions = trans
    @seconds = sec
    @nseconds = nsec
    @next_hash = n_h
    @validity = val
  end
end
