# Design assessment


```
<The goal of this document is to analyse the structure of your project, compare it with the design delivered
on April 30, discuss whether the design could be improved>
```

# Levelized structure map
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the Levelized structure map,
with all elements explosed, all dependencies, NO tangles; and report it here as a picture>
```
![](DesignAssessment_images/LevelizedStructureMap.png)

# Structural over complexity chart
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the structural over complexity chart; and report it here as a picture>
```

![](DesignAssessment_images/StructuralOverComplexityChart.png)

# Size metrics

```
<Report here the metrics about the size of your project, collected using Structure 101>
```


| Metric                                    | Measure |
| ----------------------------------------- | ------- |
| Packages                                  |    6    |
| Classes (outer)                           |   40    |
| Classes (all)                             |   40    |
| NI (number of bytecode instructions)      |   11 K  |
| LOC (non comment non blank lines of code) |  ~ 5 K  |



# Items with XS

```
<Report here information about code tangles and fat packages>
```

| Item                    | Tangled | Fat  | Size  | XS   |
| ----------------------- | ------- | ---- | ----- | ---- |
| ezshop.it.polito.ezshop | 9,81%   | 4    | 11061 | 1085 |


# Package level tangles

```
<Report screen captures of the package-level tangles by opening the items in the "composition perspective" 
(double click on the tangle from the Views->Complexity page)>
```

![](DesignAssessment_images/PackageLevelTangles2.png) ![](DesignAssessment_images/PackageLevelTangles.png)


# Summary analysis
```
<Discuss here main differences of the current structure of your project vs the design delivered on April 30>
<Discuss if the current structure shows weaknesses that should be fixed>
```
The main differences between the current structure and the design delivered on the last weeks are based on changes in implementations of classes, methods and the relationship between the interface and the rest of the program. Firstly, there is a difference in the quantity of classes added to the current version that were not considered in the design document, such as `Users`, `Product Entries`, `Credit Cards` and `Ticket Entries`. Nevertheless, there are classes that were considered in the delivered design but are not implemented in the current version, such as `Position` and `Account Book`. Secondly, there are some changes about the implementation of the called `Shop` class described in the design document and the actual version of the project. For example, the current version of this class is implemented as a database with all the methods that are used to keep the data consistent through the whole application, this means that all the Map attributes that are in the previous version of the Design Document are now implemented as relational DB and the `Shop` class became `EZShopDB`. 
Finally, the weaknesses that should be fixed in the application are related to the dependencies between the `classes` package and the `data` package, which result in a cyclic dependency.
Some examples of dependencies that create the cycle are:
```
EzShopDB -> ProductType
ProductTypeClass -> ProductType
CustomerClass -> Customer
```
