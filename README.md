# printReceipt
Java batch for printing receipt 

Customers shop from different states of US, and sales tax is applied based on location and product category.
Sales tax = roundup(price * quantity * sales tax rate)

Certain product categories are exempt from sales tax (means tax will be 0), and sales tax amount should be
rounded up to the nearest 0.05 (e.g. 1.13->1.15, 1.16->1.20, 1.151->1.20)

Input
The input of the program includes: product name, price, quantity and location of the purchase. 
E.g. Location: CA, 1 book at 17.99, 1 potato chips at 3.99

You can edit input.txt to change the input.

Output
An example output:
item                price         qty
pencils             $2.99           2
shirt              $29.99           1
subtotal:                      $32.98
tax:                            $0.55
total:                         $33.53

The output will be printed on terminal and written to output.txt file.

Product Categories
The program now only supports exemption of food tax or clothing tax. You can add/remove items of the categories on food.csv or clothing.csv.



