#!/usr/bin/ruby
require 'rubygems'
require 'fileutils'
require 'roo'

translated_keys_file = '../../hor-productregistration-localization/translated_keys.txt'

def find_language_code(language)
  case language
    when 'English'
      code = 'en'
    when 'Dutch'
      code = 'nl'
	when 'German'
	  code = 'de'	
	when 'French (FR)'
	  code = 'fr'
	when 'Portuguese (EU)'
	  code = 'fr'
	when 'Russian'
	  code = 'fr'
  end

  code
end

def generate_basic_files(languages)
  language_files = Array.new
  languages.each do |language|
    language_code = find_language_code(language)

    if language_code
      if language == 'English'
        file = Dir.pwd + "../../../Source/Library/product-registration-lib/src/main/res/values/strings.xml"
      else
        file = Dir.pwd + '../../../Source/Library/product-registration-lib/src/main/res/values-'+ language_code + '/strings.xml'
      end
      dir = File.dirname(file)
      unless File.directory?(dir)
        FileUtils.mkdir_p(dir)
      end

      open(file, 'w+') do |f|
        f.puts "<?xml version=\"1.0\" encoding=\"utf-8\"?><resources>"
        language_files.push(file)
      end
    end
  end
  language_files
end

def create_language_files(rows, translated_keys_file)
  language_files = Array.new
  languages = Array.new
  Dir.mkdir(translated_keys_file) if File.exists?(translated_keys_file)

  open(translated_keys_file, 'a') do |keys_file|
    rows.each_with_index do |row, index|
      elements = row.gsub("\"", '').split('||')
      if index == 0
        languages = elements[4, 9]
        language_files = generate_basic_files(languages)
      else
        if elements[2]
          if elements[2].length > 0 and !elements[2].eql? 'Key'
            keys_file.puts elements[2].downcase.strip
            key = get_key(elements[2])
            language_files.each_with_index do |file, index|
              open(file, 'a') do |f|
                if elements[index+4]
                  value = get_value(elements[index+4])
                  f.puts "<string name=\"#{key}\">#{value}</string>"
                else
                  if find_language_code(languages[index]) != nil
                    f.puts "<string name=\"#{key}\">MISSING #{key}</string>"
                  end
                end
              end
            end
          end
        end
      end
    end
  end
  language_files.each_with_index do |file, index|
    open(file, 'a') do |f|
      f.puts '</resources>'
    end
  end
end

def get_key(raw_key)
  key = raw_key.downcase.gsub(' ', '')
  key = key.gsub('+', '_plus').gsub('?', '_question')
  key = key.gsub('?', '_question')
  key = key.gsub('<', '&lt;').gsub('>', '&gt;')
  key = key.gsub(':', '_')
  key = key.gsub('-', '_')
  key.gsub('continue', '_continue')
end

def get_value(raw_value)
  index = 0
  value = raw_value.gsub('&', '&amp;')
  value = value.gsub(/%([\d+]?[\.]?[\d+]?[df@])/) {
    index += 1
    "%#{index}$#{get_format_from($1)}#{get_type_from($1)}"
  }
  value = value.gsub('%_', '&amp;')
  value = value.gsub('_%', '&amp;')
  value = value.gsub('<', '&lt;').gsub('>', '&gt;')
  value.gsub("'", %q(\\\'))
end

def get_type_from(string)
  if string[-1, 1] == '@'
    's'
  else
    string[-1, 1]
  end
end

def get_format_from(string)
  if string.length > 1
    string[0..-2]
  else
    ''
  end
end

File.delete(translated_keys_file) if (File.exist?(translated_keys_file))
Dir.mkdir(translated_keys_file) if File.exists?(translated_keys_file)
rows = Array.new

xls = Roo::Excelx.new('../../hor-productregistration-localization/Localization.xlsx')

xls.each_with_pagename do |name, sheet|
  (0..xls.last_row).each_with_index do |row|
    if sheet.row(row)[2]
      rows.push sheet.row(row).join('||')
    end
  end
end

create_language_files(rows, translated_keys_file)
