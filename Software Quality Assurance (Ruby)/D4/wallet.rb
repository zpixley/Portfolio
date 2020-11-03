# Class Wallet represents wallet for any active user on the blockchain
class Wallet
  attr_accessor :address, :balance

  def initialize(add, bal)
    @address = add
    @balance = bal
  end
end
