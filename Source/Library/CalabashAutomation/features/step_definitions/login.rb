When(/^I enter password fulfilling password hint criteria$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 1
sheet2=book.worksheet 'Sheet1'

c= sheet1.rows[5][2]

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end


When(/^I enter email and password that are not verified$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 3
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[4][0]
c= sheet1.rows[4][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end


When(/^I enter valid credentials in create account$/) do

i=1
index=1
id1="et_reg_fname"
id2="et_reg_email"
id3="et_reg_password"
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'

var1=sheet1.rows[9][0]
var2=sheet1.rows[9][1]
var3=sheet1.rows[9][2]

name=query("* id:'et_reg_fname'")
if name.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", name)
sleep 2
end
email1=query("* id:'et_reg_email'")
if email1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var2}", email1)
sleep 2
end

pass1=query("* id:'et_reg_password'")
if pass1.empty?
fail(msg="Error. Text box not found.") 
else 

touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{var3}", pass1)
end
end


When(/^I enter credentials for mail verification$/) do

i=1
index=1
id1="et_reg_fname"
id2="et_reg_email"
id3="et_reg_password"
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'

var1=sheet1.rows[10][0]
var2=sheet1.rows[10][1]
var3=sheet1.rows[10][2]

name=query("* id:'et_reg_fname'")
if name.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", name)
sleep 2
end

email1=query("* id:'et_reg_email'")
if email1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var2}", email1)
sleep 2
end

pass1=query("* id:'et_reg_password'")
if pass1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{var3}", pass1)
end
end



When(/^I enter valid credentials in create account for market updates$/) do

i=1
index=1
id1="et_reg_fname"
id2="et_reg_email"
id3="et_reg_password"
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'

var1=sheet1.rows[7][0]
var2=sheet1.rows[7][1]
var3=sheet1.rows[7][2]

name=query("* id:'et_reg_fname'")
if name.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", name)
sleep 2
end

email1=query("* id:'et_reg_email'")
if email1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var2}", email1)
sleep 2
end

pass1=query("* id:'et_reg_password'")
if pass1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{var3}", pass1)
end
end


When(/^I enter valid credentials in create account for no market updates$/) do

i=1
index=1
id1="et_reg_fname"
id2="et_reg_email"
id3="et_reg_password"
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'

var1=sheet1.rows[8][0]
var2=sheet1.rows[8][1]
var3=sheet1.rows[8][2]

name=query("* id:'et_reg_fname'")
if name.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", name)
sleep 2
end

email1=query("* id:'et_reg_email'")
if email1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var2}", email1)
sleep 2
end

pass1=query("* id:'et_reg_password'")
if pass1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{var3}", pass1)
end
end


Then(/^I enter credentials$/) do

i=1
index=1
id1="et_reg_fname"
id2="et_reg_email"
id3="et_reg_password"
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'

var1=sheet1.rows[1][0]
var2=sheet1.rows[1][1]
var3=sheet1.rows[1][2]

name=query("* id:'et_reg_fname'")
if name.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", name)
sleep 2
end

email1=query("* id:'et_reg_email'")
if email1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var2}", email1)
sleep 2
end

pass1=query("* id:'et_reg_password'")
if pass1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{var3}", pass1)
end
sleep 3
end


Then(/^I enter credentials for activation$/) do

i=1
index=1
id1="et_reg_fname"
id2="et_reg_email"
id3="et_reg_password"
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'

var1=sheet1.rows[2][0]
var2=sheet1.rows[2][1]
var3=sheet1.rows[2][2]

name=query("* id:'et_reg_fname'")
if name.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", name)
sleep 2
end

email1=query("* id:'et_reg_email'")
if email1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var2}", email1)
sleep 2
end

pass1=query("* id:'et_reg_password'")
if pass1.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{var3}", pass1)
end
end


Then(/^I enter valid name, email and password in create account$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[3][0]
var1=sheet1.rows[3][1]
var2=sheet1.rows[3][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
sleep 2
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", str1)
end
sleep 2
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard

keyboard_enter_text("#{var2}", str2)

end
end

When(/^I enter credentials for verification$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[12][0]
var1=sheet1.rows[12][1]
var2=sheet1.rows[12][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
sleep 2
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", str1)
end
sleep 2
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
keyboard_enter_text("#{var2}", str2)

end
sleep 3
end

When(/^I enter credentials for verification of continue$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[13][0]
var1=sheet1.rows[13][1]
var2=sheet1.rows[13][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
sleep 2
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", str1)
end
sleep 2
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
keyboard_enter_text("#{var2}", str2)

end
sleep 3
end

Then(/^I enter valid credentials in create account checking Terms & conditions$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[11][0]
var1=sheet1.rows[11][1]
var2=sheet1.rows[11][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
sleep 2
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", str1)
end
sleep 2
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
keyboard_enter_text("#{var2}", str2)

end
sleep 3
end

When(/^I enter credentials for resend button verification$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[14][0]
var1=sheet1.rows[14][1]
var2=sheet1.rows[14][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
sleep 2
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
 else 
 touch("* id:'et_reg_email'")
hide_soft_keyboard
 keyboard_enter_text("#{var1}", str1)
end
sleep 2
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{var2}", str2)
end
tap_when_element_exists("android.widget.CheckBox index:#{index.to_i-1}")
sleep 3
end

Then(/^I enter invalid name,email and password in create account$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 1
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[1][0]
var1=sheet1.rows[1][1]
var2=sheet1.rows[1][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_fname'")
hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
sleep 2
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
 else 
 touch("* id:'et_reg_email'")
hide_soft_keyboard
 keyboard_enter_text("#{var1}", str1)
end
sleep 2
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{var2}", str2)
end
tap_when_element_exists("android.widget.CheckBox index:#{index.to_i-1}")
sleep 3
end


Then(/^I enter name,valid email and password in create account page$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[4][0]
var1=sheet1.rows[4][1]
var2=sheet1.rows[4][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_fname'")
 hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
sleep 2
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", str1)
end
sleep 2
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
 keyboard_enter_text("#{var2}", str2)
end
sleep 5
end


Then(/^I enter name,invalid email and password in create account page$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 1
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[3][0]
var1=sheet1.rows[3][1]
var2=sheet1.rows[3][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_fname'")
 hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
sleep 2
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
 else 
 touch("* id:'et_reg_email'")
hide_soft_keyboard
 keyboard_enter_text("#{var1}", str1)
end
sleep 2
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
 keyboard_enter_text("#{var2}", str2)
end
end 

When(/^I enter name, existing social mail and password in create account page$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 1
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[6][0]
var1=sheet1.rows[6][1]
var2=sheet1.rows[6][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_fname'")
 hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", str1)
end
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
 keyboard_enter_text("#{var2}", str2)
end
sleep 5

end

Then(/^I enter name, existing mail and password in create account page$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 1
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[2][0]
var1=sheet1.rows[2][1]
var2=sheet1.rows[2][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_fname'")
 hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", str1)
end
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
 keyboard_enter_text("#{var2}", str2)
end
sleep 5
end


Then(/^I enter name,email and min_condition password in create account page$/) do
index=1
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[6][0]
var1=sheet1.rows[6][1]
var2=sheet1.rows[6][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_fname'")
 hide_soft_keyboard
keyboard_enter_text("#{var}", str)
end
str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{var1}", str1)
end
str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
 keyboard_enter_text("#{var2}", str2)
end
sleep 5
end


Then(/^I enter name,email and valid password in create account page$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 0
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[5][0]
var1=sheet1.rows[5][1]
var2=sheet1.rows[5][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_fname'")
 hide_soft_keyboard

keyboard_enter_text("#{var}", str)


end

str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
 else 
 touch("* id:'et_reg_email'")
hide_soft_keyboard
 keyboard_enter_text("#{var1}", str1)


end

str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
 
 keyboard_enter_text("#{var2}", str2)
end
sleep 4
end 


Then(/^I enter name,email and invalid password in create account page$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 1
sheet2=book.worksheet 'Sheet1'
var=sheet1.rows[4][0]
var1=sheet1.rows[4][1]
var2=sheet1.rows[4][2]
 
str=query("* id:'et_reg_fname'")
if str.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_fname'")
 hide_soft_keyboard

keyboard_enter_text("#{var}", str)


end

str1=query("* id:'et_reg_email'")
if str1.empty?
fail(msg="Error. Text box not found.")
 else 
 touch("* id:'et_reg_email'")
hide_soft_keyboard
 keyboard_enter_text("#{var1}", str1)


end

str2=query("* id:'et_reg_password'")
if str2.empty?
fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'et_reg_password'")
 hide_soft_keyboard
 
 keyboard_enter_text("#{var2}", str2)
end
sleep 4
end 


When(/^I enter email and password after successful activation$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 2
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[1][0]
c= sheet1.rows[1][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end

When(/^I enter email for password resetting$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 2
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[5][0]
c= sheet1.rows[5][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end

When(/^I enter invalid email for password resetting$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 3
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[6][0]
c= sheet1.rows[6][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end

When(/^I enter valid email and password in philips account$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 2
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[2][0]
c= sheet1.rows[2][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end


When(/^I enter invalid email and password in philips account$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 3
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[1][0]
c= sheet1.rows[1][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end


When(/^I leave email field empty$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 2
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[2][0]
c= sheet1.rows[2][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end


When(/^I leave password field empty$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 3
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[3][0]
c= sheet1.rows[3][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end


When(/^I enter valid email and invalid password in philips account$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 3
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[4][0]
c= sheet1.rows[4][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end


When(/^I enter invalid email and invalid password$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 3
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[5][0]
c= sheet1.rows[5][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end


When(/^I enter email in philips account$/) do
require 'spreadsheet'
Spreadsheet.client_encoding = 'UTF-8'
book = Spreadsheet.open('..\CalabashAutomation\worksheet.xls')
book.worksheets
sheet1=book.worksheet 2
sheet2=book.worksheet 'Sheet1'
b= sheet1.rows[3][0]
c= sheet1.rows[3][1]

res=query("* id:'et_reg_email'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_email'")
hide_soft_keyboard
keyboard_enter_text("#{b}", res)
sleep 2
end

res=query("* id:'et_reg_password'")
if res.empty?
fail(msg="Error. Text box not found.") 
else 
touch("* id:'et_reg_password'")
hide_soft_keyboard
keyboard_enter_text("#{c}", res)
sleep 2
end
end

