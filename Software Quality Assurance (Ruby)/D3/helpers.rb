def assign_default(x, y)
  if x == ""
    y
  else
    x
  end
end

def validate_symbols(x, y)
  if x.length == 1 && y.length == 1 && x != y
    true
  else
    false
  end
end

def validate_size(x)
  if x.to_i.to_s == x && x.to_i >= 2
    true
  else
    false
  end
end

def create_table(t_symbol, f_symbol, size)
  rows = 2**size
  cols = size + 4
  table = Array.new(rows){Array.new(cols)}
  

  for r in 0..rows-1
    curr = r
    ones = 0
    # bitwise counting
    for c in (size-1).downto(0)
      if curr % 2 == 1
        ones = ones + 1
        table[r][c] = t_symbol
      else
        table[r][c] = f_symbol
      end
      curr = curr / 2
    end

    # set all ands to false
    table[r][size] = f_symbol

    # set all ors to true
    table[r][size+1] = t_symbol

    # set all nands to true
    table[r][size+2] = t_symbol

    # set all nors to false
    table[r][size+3] = f_symbol

    # xor is true if number of true bits is odd, else false
    if ones % 2 == 1
      table[r][size+4] = t_symbol
    else
      table[r][size+4] = f_symbol
    end

    # single is true if number of true bits is 1, else false
    if ones == 1
      table[r][size+5] = t_symbol
    else
      table[r][size+5] = f_symbol
    end
  end

  # change incorrect values
  table[rows-1][size] = t_symbol
  table[0][size+1] = f_symbol
  table[rows-1][size+2] = f_symbol
  table[0][size+3] = t_symbol

  table
end