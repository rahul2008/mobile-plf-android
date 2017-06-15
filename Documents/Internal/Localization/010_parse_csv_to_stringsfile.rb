#!/usr/bin/env ruby
require 'rubygems'
require 'fileutils'
require 'roo'

input_file = "StringLocalization.xlsx"
translated_keys_file = "translated_keys.txt"
translated_keys_overview_file = "translated_keys.txt"
$localization_error_file = "Localization_errors.txt"
$largestValues = Array.new()
$language_files = Array.new
$overview_language_files = Array.new
$language_files_json = Array.new

if(ARGV[0] == '-os')
  $osType = ARGV[1]
end

if(ARGV[2] == '-file')
 $fileType = ARGV[3]
end

def getOSType()
    $osType
end

def getFileType()
    $fileType
end

def insert_error(errorMessage)
    open($localization_error_file, 'a') do |f|
        f << "LOCALIZATION_ERRORS: #{errorMessage}\n"
    end
end

def generate_overview_files()
    file = "RefAppLocalizationOverview/LanguagePackOverview.json"
    dir = File.dirname(file)
    unless File.directory?(dir)
        FileUtils.mkdir_p(dir)
    end
    open(file, 'w+') do |f|
        f.puts "\n"
        f.puts "{\"languages\":["
    end
end 

def generate_basic_files(languages)
    osType = getOSType()
    languages.each do |language|
        if (language)
            if (osType.eql? "ios")
                generate_iOS_file(language)
            elsif (osType.eql? "android")
                generate_android_file(language)
            end
        end
    end
    $language_files
end

def generate_languagepack_json_files(languages)
    languages.each do |language|
        if (language)
            language = language.sub("-", "_")
            language = language.sub("Hans-", "")
            language = language.sub("Hant-", "")
            file = "LocalizationJson/" + language + "/" + language + ".json"
            dir = File.dirname(file)
            unless File.directory?(dir)
                FileUtils.mkdir_p(dir)
            end
            open(file, 'w+') do |f|
            f.puts "\n"
            f.puts "{"
            $language_files_json.push(file)
            end
        end
    end
    $language_files_json
end

def generate_iOS_file(language)
    file = "Localization/" + language + ".lproj/Localizable.strings"
    dir = File.dirname(file)
    unless File.directory?(dir)
        FileUtils.mkdir_p(dir)
    end
    open(file, 'w+') do |f|
        f.puts "/* \nLocalizable.strings \n ReferenceApp-ios\n*/"
        f.puts "\n"
        $language_files.push(file)
    end
end

def generate_android_file(language)
    language = language.sub("-", "-r")
    language = language.sub("Hans-", "")
    language = language.sub("Hant-", "")
    file = "output/" + "values-"+language + "/strings.xml"
    dir = File.dirname(file)
    unless File.directory?(dir)
        FileUtils.mkdir_p(dir)
    end
    open(file, 'w+') do |f|
        f.puts "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n<resources>"
        $language_files.push(file)
    end
end

def get_supported_locales()
    ["en","ar","bg","cs","da","de","el","en-GB","es","es-AR","es-MX","et","fi","fr","fr-CA","he","hr","hu","it","ja","ko","lt","lv","nb","nl","pl","pt","pt-BR","ro","ru","sk","sl","sv","th","tr","vi","zh-Hans-CN","zh-Hant-TW","zh-Hant-HK"]
end

def generate_longest_string_file(largestValues)  
    osType = getOSType()
    if (osType.eql? "ios")
        generate_longest_string_file_iOS(largestValues)
    elsif (osType.eql? "android")
        generate_longest_string_file_android(largestValues)
    end
end

def generate_longest_string_file_iOS(largestValues)
    largestKeyValueFile = "Localization/" + "ca-ES"+ ".lproj/Localizable.strings"
    largestDir = File.dirname(largestKeyValueFile)
    unless File.directory?(largestDir)
        FileUtils.mkdir_p(largestDir)
    end
    open(largestKeyValueFile, 'w+') do |f|
        f.puts "/* \nLocalizable.strings \n ReferenceApp-ios\n*/"
        f.puts "\n"
        largestValues.each_with_index do |row|
            elements = row.gsub("\"","\\\"").split('||')
            if (elements[0] && elements[1])
                f.puts "\"" + elements[0].gsub(" ","") + "\" = \"" + "|" + elements[1].gsub("\n","\\n").gsub("\"","\\") + "|" + "\"" + ";"
            end
        end
    end
end

def generate_longest_string_file_android(largestValues)
    largestKeyValueFile = "output/" + "/values-ca-rES"+ "/strings.xml"
    largestDir = File.dirname(largestKeyValueFile)
    unless File.directory?(largestDir)
        FileUtils.mkdir_p(largestDir)
    end
    open(largestKeyValueFile, 'w+') do |f|
        f.puts "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n<resources>"
        largestValues.each_with_index do |row|
            elements = row.gsub("\"","\\\"").split('||')
            if (elements[0] && elements[1])
                f.puts "<string\ name=\"" +elements[0].gsub(" ","")+ "\">"+ "|"+ elements[1].gsub("\n","\\n").gsub("'", %q(\\\'))+ "|" + "</string>"
            end
        end
    end
end

def generate_longest_string_json_file(largestValues)
    largestKeyValueFile = "LocalizationJson/" + "ca_ES"+ "/ca_ES.json"
    largestDir = File.dirname(largestKeyValueFile)
    unless File.directory?(largestDir)
        FileUtils.mkdir_p(largestDir)
    end
    open(largestKeyValueFile, 'w+') do |f|
        f.puts "\n"
        f.puts "{"
        largestValues.each_with_index do |row|
            elements = row.gsub("\"","\\\"").split('||')
            if (elements[0] && elements[1])
                f.puts "\"" + elements[0].gsub(" ","") + "\" : \"" + "|" + elements[1].gsub("\n","\\n").gsub("\"","\\") + "|" + "\"" + ","
            end
        end
    end
end

def create_language_files(rows, translated_keys_file)
    language_files = Array.new
    language_files_json = Array.new
    language_codes = Array.new
    osType = getOSType()
    fileType = getFileType()
    open(translated_keys_file, 'a') do |keys_file|
        rows.each_with_index do |row, index|
            elements = row.gsub("\"","\\\"").split('||')
            if(index == 3)
                language_codes = elements[4,60]
                supported_locales = get_supported_locales()
                language_codes.each do |language_code|
                    if(supported_locales.include? language_code)
                        language_files = generate_basic_files(language_codes)
                        language_files_json = generate_languagepack_json_files(language_codes)
                        generate_longest_string_file($largestValues)
                        generate_longest_string_json_file($largestValues)
                        else
                        if (language_code.empty?)
                            insert_error("locale is missing : " + "Missing locale in excel file")
                            else
                            insert_error("locale not supported :#{language_code}")
                        end
                    end
                end
                else
                if (elements[2])
                    if elements[2].length > 0 and !elements[2].eql? "locale"
                        keys_file.puts elements[2].downcase.strip
                        language_files.each_with_index do |file, index|
                            open(file, 'a') do |f|
                                if (osType.eql? "ios")
                                    if (elements[index+4])
                                        f.puts "\"" + elements[2].gsub(" ","") + "\" = \"" + elements[index+4].gsub("\n","\\n") + "\"" + ";"
                                    else
                                        if((language_codes[index] != nil) and (elements[4] != nil))
                                            f.puts "\"" + elements[2].gsub(" ","") + "\" = \"" + language_codes[index] + elements[4] + "\"" + ";"
                                        end
                                    end
                                elsif (osType.eql? "android")
                                    if (elements[index+4] != nil && elements[index+4].scan(/%@/).length <= 1)
                                        f.puts "<string\ name=\"" + elements[2].gsub(" ","") + "\">" + elements[index+4].gsub("&","&amp;").gsub("%@","%s").gsub("\'","\\\\'") +"</string>"
                                    elsif (elements[index+4] != nil && elements[index+4].scan(/%@/).length >1)
                                        f.puts "<string\ name=\"" + elements[2].gsub(" ","") + "\"" +" formatted=\"false\""+ ">" + elements[index+4].gsub("&","&amp;").gsub("%@","%s").gsub("\'","\\\\'") +"</string>"
                                    elsif((language_codes[index] != nil) and (elements[4] != nil))
                                        f.puts "<string\ name=\"" + elements[2].gsub(" ","") + "\">" + elements[4].gsub("&","&amp;").gsub("%@","%s").gsub("\'","\\\\'") +"</string>"
                                    end
                                end
                            end
                        end
                        language_files_json.each_with_index do |file, index|
                            open(file, 'a') do |f|
                                if (elements[index+4])
                                        f.puts "\"" + elements[2].gsub(" ","") + "\" : \"" + elements[index+4].gsub("\n","\\n") + "\"" + ","
                                else
                                    if((language_codes[index] != nil) and (elements[4] != nil))
                                        f.puts "\"" + elements[2].gsub(" ","") + "\" : \"" + language_codes[index] + elements[4] + "\"" + ","
                                    end
                                end
                            end
                        end
                    end
                end
            end
        end
    end
    if (osType.eql? "android")
        appendAndroidEndTag()
        if (fileType.eql? "json")
            appendJsonEndTag()
        end
    elsif (osType.eql? "ios")
        if (fileType.eql? "json")
            appendJsonEndTag()
       end
    end
end

def create_overview_files(rows, translated_keys_overview_file)
    overview_language_files = generate_overview_files()
    language_codes = Array.new
    versions = Array.new
    urls = Array.new
    open(translated_keys_overview_file, 'a') do |keys_file|
        rows.each_with_index do |row, index|
            elements = row.gsub("\"","\\\"").split('||')
            if(index == 1)
                urls = elements[4,60]
            elsif (index == 2)
                versions = elements[4,60]
            elsif (index == 3)
                language_codes = elements[4,60]
            end
        end
    end
    File.open("RefAppLocalizationOverview/LanguagePackOverview.json", 'a+') do |f|
        language_codes.each_with_index {
            |code , index|
            code = code.sub("-", "_")
            code = code.sub("Hans-", "")
            code = code.sub("Hant-", "")
            f.puts "{" + "\"url\":\"" + urls[index] + "\"," + "\"locale\":\"" + code + "\"," + "\"remoteVersion\":\"" + versions[index] + "\"}," + "\n"
        }
        f.puts "{\"\":\"\" } ]}"
    end
end

def appendAndroidEndTag()
    Dir["output/**/*.xml"].each {
        |file|
        File.open(file, 'a+') do |file|
            file.puts "</resources>"
        end
    }
end

def appendJsonEndTag()
    Dir["LocalizationJson/**/*.json"].each {
        |file|
        File.open(file, 'a+') do |file|
            file.puts "\"\":\"\" }"
        end
    }
end

File.delete(translated_keys_file) if(File.exist?(translated_keys_file))
File.delete(translated_keys_overview_file) if(File.exist?(translated_keys_overview_file))
File.delete($localization_error_file) if(File.exist?($localization_error_file))

rows = Array.new
xls = Roo::Excelx.new(input_file)

xls.each_with_pagename do |name, sheet|
    (0..xls.last_row).each_with_index do |row|
        if(sheet.row(row)[2])
            columnValue = Array.new
            (0..xls.last_column).each_with_index do |column|
                locale = sheet.row(2)[column]
                supported_locales = get_supported_locales()
                if (supported_locales.include?(locale) || locale == nil || (locale.eql? "Key") || locale.strip.length == 0)
                    columnValue.push sheet.row(row)[column]
                end
            end
            rows.push sheet.row(row).join("||")
        end
    end
end

biggestString = String.new
key = String.new

rows.each_with_index do |row, index1|
    if (index1 < 4)
        next
    end
    elements = row.gsub("\"","\\\"").split("||")
    key = elements[2]
    biggestString = ""
    elements.each_with_index do |value,index|
        if (index < 4)
            next
        end
        if ((value && value.length > 0) || value.strip.length > 0)
            if (value.length > biggestString.length)
                biggestString = value
            end
        end
    end
    $largestValues.push key+"||"+biggestString
end


create_language_files(rows, translated_keys_file)
File.delete(translated_keys_file) if(File.exist?(translated_keys_file))

create_overview_files(rows, translated_keys_overview_file)
File.delete(translated_keys_overview_file) if(File.exist?(translated_keys_overview_file))
