# Requirements Document 

Authors: Group 38

Date: 03/04/2021

Version: 09

| Version | Changes | 
| ----------------- |:-----------|
| 01 | Added Stakeholders  |
| 02 | Added Stories and Personas  |
| 03 | Added Context diagram and interfaces  |
| 04 | Added FR and NFR  |
| 05 | Added Use case diagram  |
| 06 | Minor changes to content |
| 07 | Added Use Cases and Scenarios |
| 08 | Added System Design, Glossary and Deployment Diagram |
| 09 | Glossary rebuilt |

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
	+ [Alice](#alice)
	+ [Caroline](#caroline)
	+ [Stefano](#stefano)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases and relevant scenarios](#use-cases)
- [System design](#system-design)
- [Glossary](#glossary)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders


| Stakeholder name | Description |
|-----------------|:-----------:|
|Cashier| Uses the software to sell products |
|Customer| Is indirectly involved through the cashier to buy products |  
|Supplier| Supplies all goods and products for the shop |
|Inventory manager| Manages the inventory: amount of pieces, price and (if present) sales. |
|Customers manager| Manages customers' information and fidelity cards |
|Accounting manager|Handler of the accounting system of the shop|
|Software manager| Maintainer of the software product. It is in charge of solving problems related to the software |
|Credit Card System|Allows payments by credit cards from customers using API|
|Fidelity Card|Card used by customers to gain loyalty points|
|Product|Product on sale in the EZShop


# Context Diagram and interfaces

## Context Diagram
```plantuml
@startuml
left to right direction
actor Cashier as c
actor Product as p
actor "Inventory manager" as im
actor "Customers manager" as cm
actor "Accounting manager" as am
actor "Credit Card System" as cs
actor "Fidelity Card" as fc

c -- (EZShop)
p -- (EZShop)
cs -- (EZShop)
fc -- (EZShop)
(EZShop) -- im
(EZShop) -- cm
(EZShop) -- am


@enduml
```

## Interfaces

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|Cashier|GUI|screen, keyboard|
|Product|Bar code|laser beam|
|Inventory Manager|GUI|screen, keyboard|
|Customers Manager|GUI|screen, keyboard|
|Accounting Manager|GUI|screen, keyboard|
|Credit Card System|API|internet connection|
|Fidelity Card|Bar code|laser beam|

# Stories and personas

## Alice
Alice is a 53 years old housewife with a very numerous family. She always takes care of the food shopping for the whole family and really enjoys being rewarded with prizes the more shopping she does. Alice is very impatient and although she buys a lot of goods, she cannot bare waiting for too long at the cashier.
Alice usually goes to the food shop twice a week and gets really mad when she does not find the goods she is looking for on the shelves, sometimes when she does not manage to find a product she usually buys, she just gets out the shop and goes somewhere else.

## Caroline
Caroline is a 31 years old woman, she works at the shop as a cashier and she's always in direct contact with customers. For this reason, she is in charge of accounting the cash desk. Caroline is married and has two children so she doesn't want to stay much longer at the shop after the closing time: she wants to quickly account the cash desk and get back to her children.
In a typically working day, Caroline has to sell many products to people and she expect to have a fast calculation of the total amount of money spent from the customers. She has to ask customers for fidelity card and she also manages cashes and credit cards.
Caroline noticed that sometimes customers ask her to remove or change a product from the transaction, since they changed idea. For this reason, Caroline is interrupted and has to remove the item from the receipt.

## Stefano
Stefano is 35 years old man with professional knowledge about business, administration and operation management. He is a very organized person and is currently focused on his job because he is the owner of EZShop and he is in charge of Accounting Manager, Customers Manager and Inventory Manager.
During the working day, he wakes up at 7 am and goes directly to work after breakfast. Since he is such a responsible man, he likes to arrive earlier than the rest. After some time, he starts doing his job which consists in several actions, such as checking all the products in the inventory, manages customers' information and does accounting for incomes and expenses happening during the day. 
Stefano has high hopes about the new software that is going to be applied to the food shop, since he believes that technology is mandatory to have an optimized system of inventory and sales, and then obtain better results in terms of economic growth and organization. 

# Functional and non functional requirements

## Functional Requirements

| ID        | Description  |
| ------------- |:-------------:| 
| FR1     | Authorize and authenticate |
|  FR1.1  | Log in |
|  FR1.2  | Log out |
|  FR1.3  | Manage account |
| FR2     | Handle Customer Information |
|  FR2.1  | Add a new customer (release a new fidelity card) |
|  FR2.2  | Remove a customer |
| FR3     | Manage Inventory and Catalogue |
|  FR3.1  | Insert a new product |
|  FR3.2  | Remove product |
|  FR3.3  | Update amount of pieces of a product |
| FR4     | Read the bar code |
| FR5     | Handle sale transaction |
|  FR5.1  | Start sale transaction |
|  FR5.2  | Handle receipt |
|   FR5.3.1 | Add product to receipt |
|   FR5.3.2 | Remove product from receipt |
|  FR5.4  | Check fidelity card |
|   FR5.4.1 | Update loyalty points |
|  FR5.5  | Handle Payment by cash |
|  FR5.6 |  Handle Payment by credit card |
|   FR5.6.1 | Never keep track of credit card number |
|  FR5.7  | Update daily income |
|  FR5.8  | End sale transaction |
| FR6     | Manage accounting |
|  FR6.1  | Show incomes |
|  FR6.2  | Add an expense |

## Non Functional Requirements

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     |Usability|A very simple and intuitive GUI|all FR|
|  NFR2     |Correctness|Total price of transaction must match the sum of products' prices|FR3,FR5|
|  NFR3     |Efficiency|The transaction must be responsive and fast to compute|FR5|
| NFR4 |Reliability|Software crashes have to be <0.01% of interactions|All FR|
| NFR5 |Maintainability|Adding a new cashier requires less than 2 hours|All FR|
| NFR6 |Security|Data accessed only from authorized users|FR2,FR3,FR5,FR6|
| NFR7 |Usability|Easy detection of missing products for reorders|FR3|
|NFR8|Dependability|Accounting information always available, consistent and secured|FR6|
|NFR9|Security|Never keep track of credit card number |FR5.6.1|


# Use case diagram and use cases

## Use case diagram
```plantuml
@startuml

actor Cashier as c
actor Product as p
actor "Inventory manager" as im
actor "Customers manager" as cm
actor "Accounting manager" as am
actor "Credit Card System" as cs
actor "Fidelity Card" as fc

(EzShop) as (ez)

(Authorize and authenticate) as (aa)
(Handle customer information) as (hci)
(Manage inventory and catalogue) as (mic)
(Read the bar code) as (rbc)
(Handle sale transaction) as (hst)
(Manage accounting) as (ma)

note " All human actors have to authenticate\n to access to the system. They are not\n directly connected due to issue with the\n readability of the diagram. " as n1

n1 --> aa


(ez) ..> (aa) : <<include>>
(ez) ..> (hci) : <<include>>
(ez) ..> (mic) : <<include>>
(ez) ..> (rbc) : <<include>>
(ez) ..> (hst) : <<include>>
(ez) ..> (ma) : <<include>>


c -up-> (hst)
c -up-> (rbc) 

cm -up-> (hci)
am -up-> (ma)
im -up-> (mic)
im -up-> (rbc)



p <-up- (rbc)
p <-up- (mic)

cs -up-> (hst)

fc <-up- (hst)

fc <-up- (rbc)
fc <-up- (hci)

@enduml
```
## Use cases

### Use case 1 'Authorize and authenticate', UC1

| Actors Involved        | Inventory manager, Cashier, Customers manager, Accounting manager |
| ------------- |:-------------:| 
|  Precondition     | User has not access to the system |  
|  Post condition     | User has access to the system |
|  Nominal Scenario     | User log in the system |
|  Variants     | Wrong credentials |

##### Scenario 1.1 

| Scenario 1.1 | |
| ------------- |:-------------:| 
|  Precondition     | User has not access to data |
|  Post condition     | User has access to data |
| Step#        | Description  |
|  1           | User uses correct credentials to access to the system |  
|  2           | System gives access to user |


##### Scenario 1.2
| Scenario 1.2 | |
| ------------- |:-------------:| 
|  Precondition     | User has not access to data |
|  Post condition     | User has access to data |
| Step#        | Description  |
|  1           | User enters wrong credentials | 
|  2           | System does not give access and asks again for credentials |
| 3            | User types correct credentials |
| 4 		   | System gives access to user 


### Use case 2 'Handle customer information', UC2
| Actors Involved        | Customers manager, Fidelity Card, Customer |
| ------------- |:-------------:| 
|  Precondition     | Customer has no fidelity card <br> System has no information about customer <br> Customers manager successfully logged in the system|  
|  Post condition     | Customer receives fidelity card <br> Customer's information are stored |
|  Nominal Scenario     | Customers Manager receives customer's information and stores them connected to a specific fidelity card |
|  Variants     | // |

##### Scenario 2.1 

| Scenario 2.1 | |
| ------------- |:-------------:| 
|  Precondition     | Customer has no fidelity card <br> System has no information about customer <br> Customers manager successfully logged in the system |
|  Post condition     | Customer receives fidelity card <br> Customer's information are stored |
| Step#        | Description  |
|  1     | Customer asks for a fidelity card |  
|  2     | Customer gives to the manager his personal information |
|  3     | Customers Manager stores the information |
|  4     | Customers Manager gives to the customer the associated fidelity card |

### Use case 3 'Manage accounting', UC3
| Actors Involved        | Accounting Manager |
| ------------- |:-------------:| 
|  Precondition     | Accounting Manager successfully logged in the system |  
|  Post condition     | Manager receives total incomes/expenses <br> A new expense is registered|
|  Nominal Scenario     | Manager can look at total incomes and expenses and eventually add a new expense |
|  Variants     | // |

##### Scenario 3.1 

| Scenario 3.1 | |
| ------------- |:-------------:| 
|  Precondition     | Accounting Manager successfully logged in the system |
|  Post condition     | Manager receives total incomes/expenses <br> A new expence is registered |
| Step#        | Description  |
|  1     | Manager has an amount of expense to add |  
|  2     | Manager add it to the system |
|  3     | New value for total expenses is generated |
|  4     | Total incomes and expenses are shown |

### Use case 4 'Read the bar code', UC4
| Actors Involved        | Cashier, Inventory Manager, Fidelity Card, Product |
| ------------- |:-------------:| 
|  Precondition     | User must be authenticated <br> Product/Fidelity Card must exist in the inventory|  
|  Post condition     | Product/fidelity card successfully scanned |
|  Nominal Scenario     | The user scan the Product/fidelity card to update relative information  |
|  Variants     | // |

##### Scenario 4.1 

| Scenario 4.1 | |
| ------------- |:-------------:| 
|  Precondition     | User must be authenticated<br>Product must exist in the inventory |
|  Post condition     | Product information successfully updated |
| Step#        | Description  |
|  1     | Inventory Manager reads the bar code of the product |  
|  2     | Update amount of pieces of the product in the inventory |


##### Scenario 4.2

| Scenario 4.2 | |
| ------------- |:-------------:| 
|  Precondition     | User must be authenticated <br>Fidelity Card must exist in the system |
|  Post condition     | Fidelity Card successfully scanned |
| Step#        | Description  |
|  1     | Cashier asks for a fidelity card | 
|  2     | Cashier reads the bar code of the fidelity card |


### Use case 5 'Handle sale transaction', UC5
| Actors Involved        | Cashier, Customer |
| ------------- |:-------------:| 
|  Precondition     | Customer has products to pay |  
|  Post condition     | Customer paid for products <br> Customer receives a receipt |
|  Nominal Scenario     | Handle payments for products by reading bar code, generate total amount and receipt |
|  Variants     | Customer has fidelity card <br> Customer pay using credit card <br> Customer wants to remove a product |

##### Scenario 5.1 

| Scenario 5.1 | |
| ------------- |:-------------:| 
|  Precondition     | Customer has products to pay <br> Customer has no fidelity card <br> Customer has cash |
|  Post condition     | Customer paid for products <br> Customer receives a receipt |
| Step#        | Description  |
|  1     | A new transaction starts |
|  2     | Cashier read the bar code from product |  
|  3     | Total amount for receipt is updated |
|  4     | Amount of pieces in the inventory updated |
|  ...     | Iterate for each product |
|  5     | Cashier asks for money |
|  6     | Customer gives the cash |
|  7     | Cashier ends the payment and receipt is generated |

##### Scenario 5.2

| Scenario 5.2 | |
| ------------- |:-------------:| 
|  Precondition     | Customer has products to pay <br> Customer has fidelity card <br> Customer has credit card |
|  Post condition     | Customer paid for products <br> Customer receives a receipt <br> Loyalty points updated |
| Step#        | Description  |
|  1     | A new transaction starts |
|  2     | Customer gives fidelity card |
|  3     | Cashier read the bar code from fidelity card |
|  4     | Cashier read the bar code from product |  
|  5     | Total amount for receipt is updated |
|  6     | Amount of pieces in the inventory updated |
|  ...     | Iterate for each product |
|  7     | Cashier asks for money |
|  8     | Customer gives the credit card |
|  9     | Cashier will contact credit card system for payment | 
|  10    | Successful answer is provided from credit card system |
|  11     | Total loyalty points are updated |
|  12     | Cashier ends the payment and receipt is generated |

##### Scenario 5.3

| Scenario 5.3 | |
| ------------- |:-------------:| 
|  Precondition     | Customer has products to pay <br> Customer changes idea on an already readed product |
|  Post condition     | The unwanted product is removed from receipt |
| Step#        | Description  |
|  1     | A new transaction starts |
|  2     | Cashier read the bar code from product |  
|  3     | Total amount for receipt is updated |
|  4     | Amount of pieces in the inventory updated |
|  ...     | Iterate for each product |
|  5     | Customer wants to remove a product |
|  6     | Cashier removes the product from receipt |
|  7     | Amount of pieces in the inventory updated |
|  8     | Cashier asks for money |
|  9     | Customer gives the cash |
|  10     | Cashier ends the payment and receipt is generated |

### Use case 6 'Manage inventory and catalogue', UC6
| Actors Involved        | Product, Inventory Manager |
| ------------- |:-------------:| 
|  Precondition     | Inventory Manager successfully logged in the system <br> Product has its bar code  |  
|  Post condition     | Inventory and catalogue are updated |
|  Nominal Scenario     | Manager can modify and update the product in the inventory/catalogue |
|  Variants     | A new product is added <br> A product is removed <br> A product is updated |

##### Scenario 6.1 

| Scenario 6.1 | |
| ------------- |:-------------:| 
|  Precondition     | Inventory Manager successfully logged in the system <br> Product has its bar code <br> Product already available in the inventory |
|  Post condition     | Amount of pieces of the product updated <br> Inventory and catalogue are updated|
| Step#        | Description  |
|  1     | Manager search the product by product ID | 
|  2     | Manager updates amount of pieces of the product |
|  3     | Manager refills the product on the relative shelf |

##### Scenario 6.2 

| Scenario 6.2 | |
| ------------- |:-------------:| 
|  Precondition     | Inventory Manager successfully logged in the system <br> Product not avaiable in the inventory <br> Product not available in the catalogue <br> Product has its bar code|
|  Post condition     | Inventory and catalogue are updated <br> New product is available |
| Step#        | Description  |
|  1     | Manager inserts a new product in the catalogue |
|  2     | Manager updates amount of pieces of the product |
|  3     | Product is inserted also in the inventory |
|  4     | Manager put the product on the relative shelf |

##### Scenario 6.3 

| Scenario 6.3 | |
| ------------- |:-------------:| 
|  Precondition     | Inventory Manager successfully logged in the system, <br> Product available in the inventory and in the catalogue <br> Product has its bar code|
|  Post condition     | Inventory and catalogue are updated<br>Product is no more available|
| Step#        | Description  |
|  1     | Manager reads the bar code on the product | 
|  2     | Manager removes the product from the catalogue |
|  3     | System removes it also from the inventory |

# Glossary
```plantuml
@startuml
skinparam classAttributeIconSize 0

class "EZShop" as ez

class "Fidelity card" as fc {
	+ID
	+Loyalty points
}

class "User" as u {

}

class "Cashier" as c {
	+Username
	+Password
}

class "Inventory Manager" as im {
	+Username
	+Password
}

class "Customers Manager" as cm {
	+Username
	+Password
}

class "Accounting Manager" as am {
	+Username
	+Password
}

class "Inventory" as i {

}

class "Product" as p {
	+ID
	+Quantity
}

class "Transaction" as pu {
	+Date
}

class "Customer" as cu {
	+ Name
	+ Surname
	+ Address
}

class "Catalogue" as ca {

}

class "ProductDescriptor" as pd {
	+Name
	+ID
	+Price
	+VAT Tax
}

class "Receipt" as r {
	+Shop Name
	+Total amount
	+Products' name
	+Amount per product
	+VAT Tax
}

class "Customers Database" as cd {
}

class "Order" as o {

}

class "Bar code Reader" as bcr {

}

note "This opens the\nCredit Card System\nthat will interface\ntrough API" as N1
class "Credit Card" as cc {

}

cc -up- N1

class "Accounting Database" as ad {
}

class "IncomeDescriptor" as id {
	+Price
	+Date
}

class "ExpenceDescriptor" as ed {
	+Price
	+Date
}

class Cash {

}

ez --"*" u
ez -- ca
ez -- i
ez -- cd
ez -- ad

o "*" -- im : issued by >
o -- "1..*" pd
o "1..*" -down- Supplier

c "*" --|> u
im --|> u
cm --|> u
am --|> u

c -- "*" pu
ca -- "*" pd

i --"*" p
pd -- p
p "*" -- pu
pu --|> r
cu -- "*" pu
cd -- "*" cu
fc "0..1" --  cu

bcr -- "*" fc
bcr -- "*" p

cc "0..1" -- r
cc "0..*" -- cu

Cash "0..1" -- r
Cash "0..*" -- cu

ad -- "*" r
id "*" -- ad
ed "*" -- ad
id -- r

r -- "0..1" fc : updates >
@enduml
```

# System Design
```plantuml
@startuml
class PC
class "Credit Card Reader" as ccr
class "Bar Code Reader" as bcr
class "EZShop" as ez
class "Software" as s
class "Receipt Printer" as rp

ez o-- PC
ez o-- ccr
ez o-- bcr
ez o-- rp
s -up- PC

@enduml
```

# Deployment Diagram 
```plantuml
@startuml
node PC
node EZShopServer as es
file "EZShop backend" as b
file "EZShop application" as a

b .down.> es : <<deploy>>
es -right-"*" PC : internet link 
a --> PC : <<deploy>>

@enduml
```

