require 'sinatra'
require 'sinatra/reloader'

require_relative 'helpers.rb'

get '/input_error' do
  puts "This will go in the logging output - input_error called"
  erb :input_error # parses input_error.erb -> displays it
end

get '/address_error' do
  puts "This will go in the logging output - address_error called"
  erb :address_error # parses address_error.erb -> displays it
end

post '/save' do
  puts "This will go in the loggin output - save called"
  fname = params['fname']
  
  redirect '/saved/<%= fname %>'
end

post '/table' do
  puts "This will go in the logging output - table called (#{params})"

  t_symbol = assign_default(params['t_symbol'], 'T')
  f_symbol = assign_default(params['f_symbol'], 'F')
  size = assign_default(params['x'], '3')

  check = validate_symbols(t_symbol, f_symbol) && validate_size(size)

  if check == false
    redirect '/input_error'
  end

  size = size.to_i

  # table sizes greater than 27 cannot fit in array and sizes greater than 20 take an unreasonable time to load the view
  # cap size at 20
  if size > 20
    puts "Capping size to max value of 20"
    size = 20
  end

  puts "Params: #{t_symbol}, #{f_symbol}, #{size}"

  table = create_table(t_symbol, f_symbol, size)

  erb :table, :locals => {table: table, cols: size+6, rows: 2**size} # parses table.erb -> displays it
end

get '/' do
  puts "This will go in the logging output - index called"
  erb :index # parses index.erb -> displays it
end

not_found do
  puts "This will go in the logging output - 404 error"
  status 404
  erb :address_error, :locals => {status: status}
end