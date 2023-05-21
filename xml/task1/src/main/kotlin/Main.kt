import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import javax.xml.namespace.QName
import javax.xml.stream.*
import javax.xml.stream.events.StartElement
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource


private val personById: MutableMap<String, Person> = hashMapOf()
private val idsByName: MutableMap<String, MutableSet<String>> = hashMapOf()
private val personsWithoutID: MutableMap<String, MutableSet<Person>> = hashMapOf()
private var reader: XMLEventReader? = null
fun main(args: Array<String>) {
    val fileName = "people.xml"
    var persons: List<Person?> = parseXMLfile(fileName)
    persons = combinePeople(persons)
    var errors: List<Person> = persons.stream().filter { p: Person ->
        !p.checkConsistency(
            personById.toMap()
        )
    }.toList()
    for (problemPerson in errors) {
        val personsNoId: MutableSet<Person> = hashSetOf()
        for (personSet in personsWithoutID.values) {
            for (personNoId in personSet) if (personNoId.fullName.equals(problemPerson.fullName)) {
                personsNoId.add(personNoId)
            }
        }
        solveProblem(problemPerson, personsNoId)
    }
    for (person in personById.values) {
        if (person.spouse == null || person.childrenNumber == null) {
            person.childrenNumber = 0
        }
    }
    errors = persons.stream().filter { p: Person ->
        !p.checkConsistency(
            personById.toMap()
        )
    }.toList()
    println("Errors: " + errors.size)

    try {
        val out = ByteArrayOutputStream()
        writeToXML(out, persons)
        val xml = String(out.toByteArray(), StandardCharsets.UTF_8)
        val prettyPrintXML = formatXML(xml)

        Files.writeString(
            Paths.get("./peoplesOut.xml"),
            prettyPrintXML, StandardCharsets.UTF_8
        )
    } catch(e: Exception){
        e.printStackTrace()
    }
}

private fun combinePeople(persons: List<Person?>): List<Person> {
    println("Combining people")
    for (person in persons) {
        val id: String? = person?.id
        if (id == null) {
            val personsWitnNoId: MutableSet<Person> =
                personsWithoutID.getOrDefault(person?.fullName, hashSetOf())
            if (person != null) {
                personsWitnNoId.add(person)
            }
            personsWithoutID[person!!.fullName!!] = personsWitnNoId
            continue
        }
        if (personById.containsKey(id)) {
            val collision: Person? = personById[id]
            if (collision != null) {
                person.mergePerson(collision)
            }
        }
        personById[id] = person
    }
    for (person in personById.values) {
        val name: String? = person.fullName
        if (person.id == null || name == null) {
            throw RuntimeException("Person must have ID and name")
        }
        val ids = idsByName.getOrDefault(name, HashSet())
        ids.add(person.id!!)
        idsByName[name] = ids
    }
    val conflicts: MutableList<String> = ArrayList()
    for (person in persons) {
        val name: String = person?.fullName ?: continue
        val ids: Set<String> = idsByName[name]!!
        val id = if (ids.size == 1) ids.iterator().next() else null
        if (ids.size > 1) {
            conflicts.addAll(ids)
        }
        if (id != null) {
            person.id = id
            personById[id]?.mergePerson(person)
        }
    }
    for (person in personById.values) {
        if (person.spouse != null) {
            val spouseToAdd: MutableSet<Person> = HashSet<Person>()
            connectRelatives(spouseToAdd, person.spouse!!)
            if (!spouseToAdd.isEmpty()) {
                val spouse: Person = spouseToAdd.iterator().next()
                person.pushSpouse(spouse)
            }
        }
        val childrenToAdd: MutableSet<Person> = HashSet<Person>()
        for (child in person.children) {
            connectRelatives(childrenToAdd, child)
        }
        person.children.clear()
        childrenToAdd.forEach(person::addChild)
        val siblingsToAdd: MutableSet<Person> = HashSet<Person>()
        for (sibling in person.siblings) {
            connectRelatives(siblingsToAdd, sibling)
        }
        person.siblings.clear()
        siblingsToAdd.forEach(person::addSibling)
        val parentsToAdd: MutableSet<Person> = HashSet<Person>()
        for (parent in person.parents) {
            connectRelatives(parentsToAdd, parent)
        }
        person.parents.clear()
        parentsToAdd.forEach(person::addParent)
    }
    return personById.values.sortedWith(compareBy { it.id })
}

@Throws(TransformerException::class)
private fun formatXML(xml: String): String? {

    // write data to xml file
    val transformerFactory: TransformerFactory = TransformerFactory.newInstance()
    val transformer: Transformer = transformerFactory.newTransformer()

    // pretty print by indention
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")

    // add standalone="yes", add line break before the root element
    transformer.setOutputProperty(OutputKeys.STANDALONE, "yes")
    val source = StreamSource(StringReader(xml))
    val output = StringWriter()
    transformer.transform(source, StreamResult(output))
    return output.toString()
}

private fun writeToXML(out: OutputStream, persons: List<Person>) {
    val output: XMLOutputFactory = XMLOutputFactory.newInstance()
    val writer: XMLStreamWriter = output.createXMLStreamWriter(
        out
    )

    writer.writeStartDocument()
    writer.writeStartElement("people")

    persons.forEachIndexed { index, person ->
        writer.writeStartElement("person")
        writer.writeAttribute("id", person.id)
        writer.writeAttribute("firstname", person.firstName)
        writer.writeAttribute("lastname", person.lastName)
        writer.writeAttribute("gender", person.gender)
        writer.writeAttribute(
            "spouse", if (person.spouse == null) {
                "null"
            } else {
                person.spouse?.id
            }
        )

        writer.writeStartElement("children")
        person.children.forEach { child ->
            if (child.gender == "female")
                writer.writeStartElement("daughter")
            else
                writer.writeStartElement("son")
            writer.writeAttribute("id", child.id)
            writer.writeEndElement()
        }
        writer.writeEndElement()

        writer.writeStartElement("parents")
        person.parents.forEach { parent ->
            if (parent.gender == "female")
                writer.writeStartElement("mother")
            else
                writer.writeStartElement("father")
            writer.writeAttribute("id", parent.id)
            writer.writeEndElement()
        }
        writer.writeEndElement()

        writer.writeStartElement("siblings")
        person.siblings.forEach { sibling ->
            if (sibling.gender == "femail")
                writer.writeStartElement("sister")
            else
                writer.writeStartElement("brother")
            writer.writeAttribute("id", sibling.id)
            writer.writeEndElement()
        }
        writer.writeEndElement()

        writer.writeEndElement()
    }

    writer.writeEndElement()
    writer.writeEndDocument()
    writer.flush()
    writer.close()
}

private fun parseXMLfile(fileName: String): List<Person?> {
    val peopleList: MutableList<Person?> = ArrayList()
    var person: Person? = null
    val xmlInputFactory = XMLInputFactory.newInstance()
    try {
        reader = xmlInputFactory.createXMLEventReader(FileInputStream(fileName))
        while (reader?.hasNext()!!) {
            val xmlEvent = reader!!.nextEvent()
            if (xmlEvent.isStartElement) {
                val startElement = xmlEvent.asStartElement()
                when (startElement.name.localPart) {
                    "person" -> {
                        person = Person()
                        val idAttr = startElement.getAttributeByName(QName("id"))
                        if (idAttr != null) {
                            person.id = idAttr.value.trim { it <= ' ' }
                        }
                        val fullnameAttr = startElement.getAttributeByName(QName("name"))
                        if (fullnameAttr != null) {
                            person.fullName = fullnameAttr.value.trim { it <= ' ' }
                        }
                    }

                    "id" -> person?.id = getValue(startElement)
                    "first", "firstname" -> {
                        person?.firstName = getValue(startElement)
                    }

                    "surname", "family", "family-name" -> {
                        person?.lastName = getValue(startElement)
                    }

                    "children-number" -> person?.childrenNumber = getValue(startElement)!!.toInt()
                    "siblings-number" -> person?.siblingsNumber = getValue(startElement)!!.toInt()
                    "gender" -> person?.gender = getGender(startElement)
                    "siblings" -> {
                        val siblingsValue = getSiblingsValue(startElement)
                        if (siblingsValue != null) {
                            for (id in siblingsValue) {
                                person?.addSibling(id)
                            }
                        }
                    }

                    "brother" -> {
                        val name = getValue(startElement)
                        person?.addSibling(name, "male")
                    }

                    "sister" -> {
                        val name = getValue(startElement)
                        person?.addSibling(name, "female")
                    }

                    "son" -> {
                        val id = getValue(startElement)
                        person?.addChild(id, "male")
                    }

                    "daughter" -> {
                        val id = getValue(startElement)
                        person?.addChild(id, "female")
                    }

                    "child" -> {
                        val name = getValue(startElement)
                        if (name != null) {
                            person?.addChild(name)
                        }
                    }

                    "parent" -> {
                        val id = getValue(startElement)
                        if (id != null) {
                            person?.addParent(id)
                        }
                    }

                    "mother" -> {
                        val fullname = getValue(startElement)
                        person?.addParent(fullname, "female")
                    }

                    "father" -> {
                        val fullname = getValue(startElement)
                        person?.addParent(fullname, "male")
                    }

                    "children", "fullname" -> {}
                    "spouce" -> {
                        val fullname = getValue(startElement)
                        if (fullname != null) {
                            person?.setSpouseByName(fullname)
                        }
                    }

                    "wife" -> {
                        val id = getValue(startElement)
                        person?.setWife(id)
                    }

                    "husband" -> {
                        val id = getValue(startElement)
                        person?.setHusband(id)
                    }
                }
            }
            if (xmlEvent.isEndElement) {
                val endElement = xmlEvent.asEndElement()
                if (endElement.name.localPart == "person") {
                    peopleList.add(person)
                    peopleList.addAll(person?.children!!)
                    peopleList.addAll(person.parents)
                    peopleList.addAll(person.siblings)
                    if (person.spouse != null) peopleList.add(person.spouse!!)
                }
            }
        }
    } catch (exc: Exception) {
        exc.printStackTrace()
    }
    return peopleList.toList()
}

private fun getSiblingsValue(event: StartElement): Array<String>? {
    val iterator = event.attributes
    if (iterator.hasNext()) {
        val attribute = iterator.next()
        return attribute.value.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }
    return null
}

@Throws(XMLStreamException::class)
private fun getValue(event: StartElement): String? {
    val value: String
    val iterator = event.attributes
    value = if (iterator.hasNext()) {
        val attribute = iterator.next()
        attribute.value.trim { it <= ' ' }
    } else {
        val charsEvent = reader!!.nextEvent()
        if (charsEvent.isEndElement) {
            return null
        }
        reader!!.nextEvent()
        charsEvent.asCharacters().data.trim { it <= ' ' }
    }
    return if (value == "NONE" || value == "UNKNOWN") {
        null
    } else {
        value
    }
}

@Throws(Exception::class)
private fun parseGender(gender: String): String {
    return when (gender) {
        "male", "M" -> "male"
        "female", "F" -> "female"
        else -> throw Exception()
    }
}

@Throws(Exception::class)
private fun getGender(event: StartElement): String {
    val iterator = event.attributes
    return if (iterator.hasNext()) {
        val attribute = iterator.next()
        parseGender(attribute.value.trim { it <= ' ' })
    } else {
        val charsEvent = reader!!.nextEvent()
        reader!!.nextEvent()
        val gender = charsEvent.asCharacters().data.trim { it <= ' ' }
        parseGender(gender)
    }
}

private fun connectRelatives(peopleToAdd: MutableSet<Person>, person: Person) {
    if (person.id != null) {
        val id: String = person.id!!
        personById[id]?.let { peopleToAdd.add(it) }
    } else if (person.fullName != null) {
        val name: String = person.fullName!!
        val ids: Set<String> = idsByName[name]!!
        if (ids.size == 1) {
            val id = ids.iterator().next()
            personById[id]?.let { peopleToAdd.add(it) }
        }
    }
}

private fun solveProblem(person: Person, others: Set<Person>) {
    println("Solving problems")
    if (person.gender == null) {
        println("Fixing gender 1 $person")
        for (other in others) {
            if (other.spouse != null && person.id == other.spouse!!.id) {
                if (other.spouse!!.gender == "male")
                    person.gender = "male"
                else if (other.spouse!!.gender == "female")
                    person.gender = "female"
            }
        }
    }
    if (person.gender == null) {
        println("Fixing gender 2 $person")
        for (other in others) {
            for (otherSibling in other.siblings) {
                for (personSibling in person.siblings) {
                    if (personSibling.id.equals(otherSibling.id)) {
                        if (other.gender != null) {
                            person.gender = other.gender
                        }
                    }
                }
            }
        }
    }
    if (person.gender == null) {
        println("Fixing gender 3 $person")
        var femaleCount = 0
        var maleCount = 0
        for (other in others) {
            if (other.gender != null && other.gender.equals("male")) maleCount++
            if (other.gender != null && other.gender.equals("female")) femaleCount++
        }
        if (femaleCount == 0 && maleCount > 0) person.gender = "male"
        if (maleCount == 0 && femaleCount > 0) person.gender = "female"
    }
    if (person.spouse == null) {
        println("Fixing spouse $person")
        for (other in others) {
            if (other.spouse != null && person.id == other.spouse!!.id) {
                person.pushSpouse(other)
                if (person.spouse!!.id == null) {
                    for (innerOther in others) {
                        if (person.spouse != null &&
                            person.spouse!!.fullName.equals(innerOther.fullName) && innerOther.spouse != null &&
                            innerOther.spouse!!.id != null &&
                            innerOther.spouse!!.id != person.id
                        ) {
                            person.pushSpouse(personById[innerOther.spouse!!.id])
                        }
                    }
                }
                break
            }
        }
    }
    if (person.spouse != null && person.spouse!!.id == null) {
        println("Fixing children $person")
        val spouces: Set<String> = idsByName[person.spouse!!.fullName]!!
        for (spouce in spouces) {
            for (possibleChild in personById[spouce]?.children!!) {
                if (person.children.contains(possibleChild)) {
                    person.resetSpouse()
                    person.pushSpouse(personById[spouce])
                    break
                }
            }
            if (person.spouse!!.id != null) {
                break
            }
        }
    }
    if (person.spouse != null && person.spouse!!.gender == null) {
        println("Fixing spouse gender $person")
        if (person.gender == "male") {
            person.spouse!!.gender = ("female")
        } else if (person.gender == "female") {
            person.spouse!!.gender = ("male")
        }
    }
    if (person.childrenNumber == null || person.childrenNumber!! > person.children.size) {
        println("Fixing children in spouse $person")
        if (person.spouse != null) {
            for (spouseChild in person.spouse!!.children) {
                person.children.add(spouseChild)
                spouseChild.addParent(person)
            }
        }
    }
    if (person.spouse != null && person.spouse!!.id == null) {
        if (person.spouse!!.siblingsNumber != null && person.spouse!!.siblingsNumber == 1) {
            val spouceSiblings: Set<String> = idsByName[person.spouse!!.fullName]!!
            if (spouceSiblings.size == 2) {
                for (sibling in spouceSiblings) {
                    if (person.id == sibling) continue
                    if (personById[sibling]?.id != null) {
                        person.pushSpouse(personById[sibling])
                        person.spouse?.pushSpouse(personById[person.id])
                    }
                }
            }
        }
    }
}
