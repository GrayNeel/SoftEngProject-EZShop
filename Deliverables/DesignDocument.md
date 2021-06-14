# Design Document 


Authors: Group 38

Date: 08/06/2021

Version: 04

| Version | Changes | 
| ----------------- |:-----------|
| 01 | Added High Level Design and Low Level Design  |
| 02 | Added Verification Traceability Matrix and Verification sequence diagrams |
| 03 | Modified Low Level Design due to Requirements update |
| 04 | Added product class |

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

class Shop {
    +productTypeMap
    +customersMap
    +saleTransactionsMap
    +ordersMap
    +accountBook
}

note "Persistent data" as N1

N1 .down. Shop
ShopInterface -down- Shop : <<implements>>

class ShopInterface {


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

class BalanceOperation {
 +description
 +amount
 +date

 +updateAmount()
}

Order --|> BalanceOperation


class ProductType{
    +barCode
    +description
    +price
    +quantity
    +discountRate
    +notes
    +position

    +addPosition()
    +updatePosition()
    +updatePrice()
    +updateQuantity()
    +updateDiscountRate(string,double)
}

class Product {
    +RFID
}

ProductType - "*" Product

note "Persistent data" as N2
N2 -down- ProductType
Shop -- "*" ProductType
Shop -- "*" Order
Shop -- "*" SaleTransaction
Shop -- "*" Customer

class SaleTransaction {
    +ID 
    +date
    +time
    +cost
    +paymentType
    +discountRate
    +productTypeMap
    +LoyaltyCard

    +addProduct(string,int)
    +deleteProduct(string,int)
    +addDiscountRate(double)
    +computePointsForSale()
    +modifyPointsOnCard(string,int)
    +updateCost()
    +verifyPaymentSuccess()
}
SaleTransaction -- "*" ProductType

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

LoyaltyCard "0..1" -- Customer

SaleTransaction "*" -- "0..1" LoyaltyCard

class Position {
    +positionID
    +aisleID
    +rackID
    +levelID
    +isFree
}

ProductType -- "0..1" Position

class Order {
  +ID
  +supplier
  +pricePerUnit
  +quantity
  +status
  +productType

  +updateStatus()
}

Order "*" -- ProductType

class ReturnTransaction {
  +quantity
  +returnedValue
  +SaleTransaction
  +ProductType
  +paymentType

  +addReturnedProduct(string,int)
  +computeReturnedValue()
}

ReturnTransaction "*" -- SaleTransaction
ReturnTransaction "*" -- ProductType

class AccountBook {
    +balanceOperationsMap
}

AccountBook -- Shop
AccountBook -- "*" BalanceOperation

@enduml
```







# Verification traceability matrix

|     | Shop | SaleTransaction | ProductType | Position | LoyaltyCard | Customer | Order | ReturnTransaction | BalanceOperation | Product |
| --- | :----: | :---------------: | :-----------: | :--------: | :-----------: | :--------: | :-----: | :-----------------: | :--------------------: | :---:|
| FR1 |X| | | | | | | | | |
| FR2 |X| |X| | | | | | |X|
| FR3 |X| |X|X| | |X| |X|X|
| FR4 |X| | | |X|X| | | | |
| FR5 |X|X|X| | | | |X|X|X|
| FR6 |X|X| | | | | | | | |
| FR7 |X| | | | | | | |X| |




# Verification sequence diagrams 

### Scenario 4.1
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