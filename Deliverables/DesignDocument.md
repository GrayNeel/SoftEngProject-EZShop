# Design Document 


Authors: Group 38

Date: 24/04/2021

Version: 01


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

_Architecture_:
* EZShop is a **stand-alone** application.

_Architectural patterns_:
* MVC (because there is a GUI)
* layered - 3 tiered

```plantuml
@startuml

package "EzShop" as ez {}
package "EzShop Data" as mt {}
package "EzShop Application Logic" as at {}
package "EzShop GUI" as GUI {}

mt -down-|> ez
at -down-|> ez
GUI -down-|> ez

@enduml
```

# Low level design

```plantuml
@startuml

left to right direction
class Shop {
    +ProductTypeList
    +CustomersList
    +TicketsList
    +OrdersList
    +SaleTransactionList
    +FinancialTransactionList

    +createProductType(string, string, double,string)
    +updateProduct(integer,string,string,double,string)
    +deleteProductType(integer)
    +getProductTypeByBarCode(string)
    +createUser(string, string, string)
    +deleteUser(integer)
    +updateUserRights(integer,string)
    +issueOrder(string,int,double)
    +payOrderFor(string,int,double)
    +payOrder(integer)
    +recordOrderArrival(integer)
    +defineCustomer(string)
    +modifyCustomer(integer, string, string)
    +deleteCustomer(integer, string, string)
    +createCard()
    +attachCardToCustomer(string,integer)
    +detachCardFromCustomer(integer)
    +modifyPointsOnCard(string,integer)
    +login(string,string,string)
    +logout()
    +startSaleTransaction()
    +closeSaleTransaction(integer)
    +startReturnTransaction(integer)
    +returnProduct(integer, integer, integer)
    +endReturnTransaction(integer, boolean)
    +deleteReturnTransaction(integer)
    +deleteSaleTicket(integer)
    +getCreditsAndDebits(LocalDate, Localdate)
}

note "Persistent data that\nhave to be implemented\nprobabily as a DB" as N1

N1 .. Shop

class FinancialTransaction {
 +description
 +amount
 +date

 +updateAmount()
}

Order --|> FinancialTransaction


class ProductType{
    +barCode
    +description
    +price
    +quantity
    +discountRate
    +notes

    +addPosition()
    +updatePosition()
    +updatePrice()
    +updateQuantity()
    +updateDiscountRate(string,double)
}

Shop - "*" ProductType

class SaleTransaction {
    +ID 
    +date
    +time
    +cost
    +paymentType
    +discountRate
    +ProductsMap

    +addProduct(string,int)
    +deleteProduct(string,int)
    +addDiscountRate(double)
    +computePointsForSale()
    +modifyPointsOnCard(string,int)
    +updateCost()
    +verifyPaymentSuccess()
    +printTicket()
}
SaleTransaction - "*" ProductType

class LoyaltyCard {
    +ID
    +points
}

class Customer {
    +name
    +surname
    +LoyaltyCardID

    +addLoyaltyCard()
    +removeLoyaltyCard()
    +updateName()
    +updateSurname()
}

LoyaltyCard "0..1" - Customer

SaleTransaction "*" -- "0..1" LoyaltyCard

class Position {
    +positionID
    +aisleID
    +rackID
    +levelID
    +isFree
}

ProductType - "0..1" Position

class Order {
  +ID
  +supplier
  +pricePerUnit
  +quantity
  +status

  +updateStatus()
}

Order "*" - ProductType

class ReturnTransaction {
  +quantity
  +returnedValue
  +SaleTransactionID
  +ProductType
  +paymentType

  +addReturnedProduct(string,int)
  +computeReturnedValue()
}

ReturnTransaction "*" - SaleTransaction
ReturnTransaction "*" - ProductType

@enduml
```







# Verification traceability matrix

|     | Shop | SaleTransaction | ProductType | Position | LoyaltyCard | Customer | Order | ReturnTransaction | FinancialTransaction |
| --- | :----: | :---------------: | :-----------: | :--------: | :-----------: | :--------: | :-----: | :-----------------: | :--------------------: |
| FR1 |X| | | | | | | | |
| FR2 |X| |X| | | | | | |
| FR3 |X| |X|X| | |X| |X|
| FR4 |X| | | |X|X| | | |
| FR5 |X|X|X| | | | |X|X|
| FR6 |X|X| | | | | | | |
| FR7 |X| | | | | | | |X|





# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

### Scenario 6.1
```plantuml
@startuml
Shop -> Customer : 1. defineCustomer()
activate Customer
Customer -> Customer : 2.setName()
Customer -> Customer : 3.setSurname()
Customer -> Shop : 4. returnCustomer()
deactivate Customer
@enduml
```

### Scenario 6.1
```plantuml
@startuml
Shop -> SaleTransaction: 1: startSaleTransaction()
activate SaleTransaction
SaleTransaction -> ProductType: 2: getProductType()
ProductType -> ProductType: 3: updateQuantity()
SaleTransaction <- ProductType: 4: addProduct()
SaleTransaction <- SaleTransaction : 5: closeSaleTransaction()
deactivate SaleTransaction
SaleTransaction <- SaleTransaction : 6: setPaymentType()
SaleTransaction -> Shop : 7: verifyPaymentSuccess()
Shop -> CreditCardSystem : 8: payment()
Shop <- CreditCardSystem : 9: paymentSuccess()
SaleTransaction -> Printer : 10. printTicket()
@enduml
```

### Scenario 8.1
```plantuml
@startuml
Shop -> ReturnTransaction : 1: startReturnTransaction()
activate ReturnTransaction
ReturnTransaction -> ReturnTransaction : 2: setSaleTransactionID()
ReturnTransaction -> ProductType : 3: getProductType()
ReturnTransaction -> ReturnTransaction : 4. setProductType()
ReturnTransaction -> ReturnTransaction : 5. setQuantity()
ReturnTransaction -> ProductType : 6. updateQuantity()
ReturnTransaction -> ReturnTransaction : 7. closeReturnTransaction()
deactivate ReturnTransaction
ReturnTransaction -> ReturnTransaction : 8. setPaymentType()
Shop -> CreditCardSystem : 9: returnPayment()
Shop <- CreditCardSystem : 10: returnPaymentSuccess()
@enduml
```