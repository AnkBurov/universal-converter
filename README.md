# Universal converter

Service that gives possibility of transformation of arbitrary CSV files into predetermined XML files.
Currently only the backend of the service is available.

## Workflow

1) User uploads CSV file into service and receives list of columns from the XML
2) User chooses desired destination XML scheme and receives list of fields that are needed to form an XML file
3) User maps fields that are needed to form an XML file with columns from CSV file (or specifies absolute values)
4) User passes mapping and the desired schema into a service and converter starts working
5) After the XML is generated, it is optionally validated under desired XSD schema.
6) User receives list of converted XMLs (one or many, depending on the implementation for the schema converter)

## Adding the new Schema

1) First of all, need to create new Schema member in the class **Schema**
    ```kotlin
    enum class Schema(val schemaName: String) {
        XML_EXAMPLE_V1("xmlExample_v1.xsd"),
        NEW_SCHEMA("newSchemaXsd.xsd");
    }
    ```
    where NEW_SCHEMA is the name of the schema and newSchemaXsd.xsd is the name of file in the project directory
     **src/main/resources/schema/validation**
     
2) Add new XSD schema validation file into directory from the previous step (name has to be same)
     ```
        see schema/validation/xmlExample_v1.xsd
     ```

3) Create new implementation of **ConverterStrategy** interface and describe there how to create XML file.
ConverterStrategy receives list of trees as an argument where each tree is the intermediate representation
of each line in the CSV file. Tree is the hierarchical representation of line in the CSV file where each node has
a key and value from the CSV file. Trees are formed automatically.
    
    At this step, any transformations can be done with the data. For example, sum or multiply values of few fields
to form new field.

    All node names should be listed in companion object block. If some nodes are required, then it should 
    be reflected in the NodeName object, see YEAR example.
 
    New implementations of **ConverterStrategy** should also implement getUsingNodeNames() and getSchema() methods:
        - getSchema() - get instance of Schema member from the 1 step.
        - getUsingNodeNames() - get list of used tree keys in the XML forming
    ```kotlin
        @Component
        class XmlExampleConverterStrategy : ConverterStrategy {
        
            override fun convertTrees(trees: List<Tree>): List<String> {
                val xmlBody = xml("root") {
                    xmlns = XMLNS
        
                    for (tree in trees) {
                        "cars" {
                            "year" {
                                -tree.getValue(YEAR).toString()
                            }
                            "brand" {
                                -tree.getValue(BRAND).toString()
                            }
                            "model" {
                                -tree.getValue(MODEL).toString()
                            }
        
                            "something" {
                                attribute("isWhatever", "sure")
                                "nested" {
                                    -tree.getValue(SOMETHING_NESTED).toString()
                                }
                            }
                        }
                    }
                }
        
                return listOf(XML_HEADER + System.lineSeparator() + xmlBody.toString())
            }
        
            override fun getUsingNodeNames() = usingNodeNamesField
        
            override fun getSchema() = Schema.XML_EXAMPLE_V1
        
            companion object {
                const val XMLNS = "some"
        
                val YEAR = NodeName("year", isRequired = true)
                val BRAND = NodeName("brand")
                val MODEL = NodeName("model")
                val SOMETHING_NESTED = NodeName("something/nested")
        
                val usingNodeNamesField = this.getNodeNameFields()
            }
        }
    ```
    
4) Create new implementation of **SchemaValidationStrategy** or **SingleXmlValidationStrategy** and define 
using Schema member from the 1 step and XSD file name from the 2 step:
    ```kotlin
        @Component
        class XmlExampleValidationStrategy : SingleXmlValidationStrategy() {
        
            private val xsdSchemaFile = ClassPathResource("schema/validation/${getSchema().schemaName}").file
        
            override fun getXsdSchemaFile() = xsdSchemaFile
        
            final override fun getSchema() = Schema.XML_EXAMPLE_V1
        }
    ```
    
5) To confirm that created converter strategy is valid under given XSD schema from the 2 step, simple test
should be written. Define expected tree structure and instances of classes created in 4 and 5 respectively. 

    If generated XML is invalid under XSD validation or some required values in tree are absent then test will fail

    ```kotlin
        class XmlExampleConverterStrategyTest {
        
            @Test
            fun testConversion() {
        
                testConverter {
                    +treeDsl {
                        "year"("2012")
        
                        "brand"("Tesla")
        
                        "Model S"("Tesla")
        
                        "something" {
                            "nested"("nested")
                        }
                    }
                    converterStrategy = XmlExampleConverterStrategy()
                    schemaValidationStrategy = XmlExampleValidationStrategy()
                }
            }
        }
    ```

6) Commit and deploy, new schema will be available for usage.