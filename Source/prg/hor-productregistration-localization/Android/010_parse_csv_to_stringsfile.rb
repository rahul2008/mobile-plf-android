#!/usr/bin/env ruby
require 'rubygems'
require 'fileutils'
require 'roo'

input_file = "export.csv"
translated_keys_file = "translated_keys.txt"

if(ARGV[0] == '-file')
  input_file = ARGV[1]
end

def generate_basic_files(languages)
  language_files = Array.new
  languages.each do |language|
	if (language)
	   if language == 'en'
        file = Dir.pwd + "../../../Source/Library/product-registration-lib/src/main/res/values/strings.xml"
      else
        file = Dir.pwd + '../../../Source/Library/product-registration-lib/src/main/res/values-'+ language + '/strings.xml'
      end		
       dir = File.dirname(file)
        unless File.directory?(dir)
          FileUtils.mkdir_p(dir)
        end
      
        open(file, 'w+') do |f|
        f.puts "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n<resources>"
          language_files.push(file)
        end
    end
  end
  language_files
end

def create_language_files(rows, translated_keys_file)
  language_files = Array.new
  language_codes = Array.new
  open(translated_keys_file, 'a') do |keys_file|
    rows.each_with_index do |row, index|
      elements = row.gsub("\"","\\\"").split('||')
      if(index == 1)
        language_codes = elements[4,53]
        language_files = generate_basic_files(language_codes)
      else
        if (elements[2])
           if elements[2].length > 0 and !elements[2].eql? "Key"
            keys_file.puts elements[2].downcase.strip
            language_files.each_with_index do |file, index|
              open(file, 'a') do |f|
                if (elements[index+4] != nil && elements[index+4].scan(/%@/).length <= 1)
				
                  f.puts "<string\ name=\"" + elements[2].gsub(" ","") + "\">" + elements[index+4].gsub("&","&amp;").gsub("%@","%s").gsub("\'","\\\\'") +"</string>"
				else if (elements[index+4] != nil && elements[index+4].scan(/%@/).length >1)
				  f.puts "<string\ name=\"" + elements[2].gsub(" ","") + "\"" +" formatted=\"false\""+ ">" + elements[index+4].gsub("&","&amp;").gsub("%@","%s").gsub("\'","\\\\'") +"</string>"
                else
					if((language_codes[index] != nil) and (elements[4] != nil))
                  f.puts "<string\ name=\"" + elements[2].gsub(" ","") + "\">" + elements[4].gsub("&","&amp;").gsub("%@","%s").gsub("\'","\\\\'") +"</string>"
                    end
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
            f.puts "</resources>"
	end
end
  
end

File.delete(translated_keys_file) if(File.exist?(translated_keys_file))

rows = Array.new
xls = Roo::Excelx.new('../../hor-productregistration-localization/Localization.xlsx')

xls.each_with_pagename do |name, sheet|
  (0..xls.last_row).each_with_index do |row|
    if(sheet.row(row)[2])
      rows.push sheet.row(row).join("||")
    end
  end
end

create_language_files(rows, translated_keys_file)
File.delete(translated_keys_file) if(File.exist?(translated_keys_file))


