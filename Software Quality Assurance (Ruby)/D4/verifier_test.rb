require 'simplecov'
SimpleCov.start
require 'set'
require 'minitest/autorun'
require_relative 'block'
require_relative 'wallet'
require_relative 'helper'

# Class VerifierTest contains all the tests to verify functionaility of Verifier.rb
class VerifierTest < Minitest::Test
  # Create new instance of the helper class to use for testing
  def setup
    @testing = Helper.new
  end

  # Testing to see if there are the correct amount of parts to the block
  # EXPECTING RETURN VALUE OF 1
  def test_check_format_1
    @testing.curr_str = '0|0SYSTEM>569274(100)|1553184699.650330000|288d'
    value = @testing.check_format
    assert_equal(1, value)
  end

  # Testing invlaid transaction format
  # EXPECTING RETURN VALUE OF 3
  def test_check_format_2
    @testing.curr_str = '0|0|SYSTEM>569274(100)::|1553184699.650330000|288d'
    value = @testing.check_format
    assert_equal(3, value)
  end

  # Testing invlaid timestamp format
  # EXPECTING RETURN VALUE OF 5
  def test_check_format_3
    @testing.curr_str = '0|0|SYSTEM>569274(100)|1553184699650330000|288d'
    value = @testing.check_format
    assert_equal(5, value)
  end

  # Testing to see if the hash values are formatted incorrectly
  # EXPECTED RETURN VALUE OF 9
  def test_check_format_4
    @testing.curr_str = '0|0|SYSTEM>569274(100)|1553184699.650330000|2888d'
    value = @testing.check_format
    assert_equal(9, value)
  end

  #---------------TESTS FOR parse_block----------------------------------------------
  # Testing to see what happens with incorrect line number
  # EXPECTING validity VALUE OF 2
  def test_parse_block_1
    @testing.curr_str = '0|0|SYSTEM>569274(100)|1553184699.650330000|288d'
    @testing.p_s = 0
    @testing.p_ns = 0
    @testing.p_h = '0'
    @testing.curr_line = 1
    value = @testing.parse_block
    assert_equal(2, value.validity)
  end

  # Testing to see if the previos hash does not match block's previous hash
  # EXPECTING A validity VALUE OF 8
  def test_parse_block_2
    @testing.curr_str = '9|7777|402207>794343(10):402207>780971(13):794343>236340(16)
                        :717802>717802(1):SYSTEM>689881(100)|1553184699.691433000|7ad7'
    @testing.p_s = 0
    @testing.p_ns = 0
    @testing.p_h = 'a91'
    @testing.curr_line = 9
    value = @testing.parse_block
    assert_equal(7, value.validity)
  end

  # Testing to see if the previous timestamp is less than the present timestamp
  # EXPECTING validity value of 6
  def test_parse_block_3
    @testing.curr_str = '9|a91|402207>794343(10):402207>780971(13):794343>236340(16)
                        :717802>717802(1):SYSTEM>689881(100)|1553184699.0|9852'
    @testing.p_s = 1_553_184_699
    @testing.p_ns = 685_386_000
    @testing.p_h = 'a91'
    @testing.curr_line = 9
    value = @testing.parse_block
    assert_equal(6, value.validity)
  end

  # Testing to see if the calculated hash matched the block hash
  # EXPECTING THE validity VALUE TO BE 7
  def test_parse_block_4
    @testing.curr_str = '9|a91|402207>794343(10):402207>780971(13):794343>236340(16)
                        :717802>717802(1):SYSTEM>689881(100)|1553184699.691433000|abcd'
    @testing.p_s = 1_553_184_699
    @testing.p_ns = 685_386_000
    @testing.p_h = 'a91'
    @testing.curr_line = 9
    value = @testing.parse_block
    assert_equal(7, value.validity)
  end

  #------------------Tests for Calc_hash-----------------------------------------------------------------
  # Testing to see of the hash value being return for the block is correct
  # Expecting
  def test_calc_hash_1
    value = @testing.calc_hash('0|0|SYSTEM>569274(100)|1553184699.650330000')
    assert_equal('288d', value)
  end

  #------------------Tests for complete_transaction ------------------------------------------------
  # Testing to see if the transaction is formatted incorrectly
  # Expecting a return value of 1
  def test_complete_transaction_1
    @negs = []
    value = @testing.complete_transaction('SYSTEM569274(100)')
    assert_equal(1, value)
  end

  # Testing to see if the addresses are validly formatted
  # Expecting the return value to be 1
  def test_complete_transaction_2
    @negs = []
    value = @testing.complete_transaction('SYSTEM>5569274(100)')
    assert_equal(1, value)
  end

  #-------------------Tests for verify blockchain Errors-----------------------------------------------------------
  def test_verify_blockchain_1
    value = @testing.verify_blockchain(File.read('invalid_format.txt').split("\n"))
    assert_equal(3, value)
  end

  def test_verify_blockchain_2
    value = @testing.verify_blockchain(File.read('bad_number.txt').split("\n"))
    assert_equal(2, value)
  end

  def test_verify_blockchain_3
    value = @testing.verify_blockchain(File.read('bad_prev_hash.txt').split("\n"))
    assert_equal(8, value)
  end

  def test_verify_blockchain_4
    value = @testing.verify_blockchain(File.read('bad_block_hash.txt').split("\n"))
    assert_equal(7, value)
  end

  def test_verify_blockchain_5
    value = @testing.verify_blockchain(File.read('bad_timestamp.txt').split("\n"))
    assert_equal(6, value)
  end

  def test_verify_blockchain_6
    value = @testing.verify_blockchain(File.read('invalid_transaction.txt').split("\n"))
    assert_equal(4, value)
  end

  def test_verify_blockchain_7
    value = @testing.verify_blockchain(File.read('sample.txt').split("\n"))
    assert_equal(0, value)
  end

  def test_verify_blockchain_8
    value = @testing.verify_blockchain(File.read('bad_block.txt').split("\n"))
    assert_equal(1, value)
  end

  def test_verify_blockchain_9
    value = @testing.verify_blockchain(File.read('no_timestamp.txt').split("\n"))
    assert_equal(5, value)
  end

  def test_verify_blockchain_10
    value = @testing.verify_blockchain(File.read('invalid_hash_length.txt').split("\n"))
    assert_equal(9, value)
  end
end
