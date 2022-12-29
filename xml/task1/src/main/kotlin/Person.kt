import java.util.*
import java.util.stream.Stream

class Person(var id: String? = null) {
    var firstName: String? = null
        set(value) {
            if (field != null && field != value) {
                throw Error("Firstname already set")
            }
            field = value
        }
    var lastName: String? = null
        set(value) {
            if (field != null && field != value) {
                throw Error("Lastname already set")
            }
            field = value
        }
    var gender: String? = null
        set(value) {
            if (field != null && field != value) {
                throw Error("Gender already set")
            }
            field = value
        }
    var spouse: Person? = null
    var childrenNumber: Int? = null
        set(value) {
            if (field != null && field != value) {
                throw Error("childrenNumber already set")
            }
            field = value
        }
    var siblingsNumber: Int? = null
        set(value) {
            if (field != null && field != value) {
                throw Error("siblingsNumber already set")
            }
            field = value
        }

    val parents: MutableSet<Person> = HashSet()
    val siblings: MutableSet<Person> = HashSet()
    val children: MutableSet<Person> = HashSet()

    var fullName: String?
        get() = if (firstName != null && lastName != null) {
            "$firstName $lastName"
        } else null
        set(value) {
            val words = value?.split(" +".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            firstName = words?.get(0)
            lastName = words?.get(1)
        }
    fun addSibling(sibling: Person) {
        if (sibling.siblingsNumber == null) {
            sibling.siblingsNumber = siblingsNumber
        }
        if (siblingsNumber == null) {
            siblingsNumber = sibling.siblingsNumber
        }
        siblings.add(sibling)
        sibling.siblings.add(this)
    }

    fun addSibling(id: String?) {
        addSibling(Person(id))
    }

    fun addSibling(name: String?, gender: String?) {
        val sibling = Person()
        sibling.fullName = name
        sibling.gender = gender
        addSibling(sibling)
    }

    fun addChild(child: Person) {
        children.add(child)
        child.parents.add(this)
    }

    fun addChild(name: String) {
        val child = Person()
        child.fullName = name
        addChild(child)
    }

    fun addChild(id: String?, gender: String?) {
        val child = Person(id)
        child.gender = gender
        addChild(child)
    }
    fun addParent(parent: Person) {
        parent.children.add(this)
        parents.add(parent)
    }

    fun addParent(id: String?) {
        val parent = Person(id)
        addParent(parent)
    }

    fun addParent(fullname: String?, gender: String?) {
        val parent = Person()
        parent.fullName = fullname
        parent.gender = gender
        addParent(parent)
    }

    fun resetSpouse() {
        spouse = null
    }

    fun pushSpouse(value: Person?){
        if (spouse != null && spouse!!.id != null && value != spouse) {
            throw Error("Spouce already set")
        }
        spouse = value
        if (value != null) {
            value.spouse = this
        }
        if (spouse!!.childrenNumber != null) {
            childrenNumber = spouse!!.childrenNumber!!
        } else if (childrenNumber != null) {
            if (value != null) {
                value.childrenNumber = childrenNumber!!
            }
        }
    }
    fun setSpouseByName(name: String) {
        if (spouse != null && name != spouse!!.firstName) {
            throw Error("Spouse already set")
        }
        val spouseNew = Person()
        spouseNew.fullName = name
        spouse = spouseNew
    }

    fun setWife(id: String?) {
        if (spouse != null && id != spouse!!.id) {
            throw Error("Wife already set")
        }
        val wife = Person(id)
        wife.gender = "female"
        spouse = wife
        gender = "male"
    }

    fun setHusband(id: String?) {
        if (spouse != null && id != spouse!!.id) {
            throw Error("Husband already set")
        }
        val husband = Person(id)
        husband.gender = "male"
        spouse = husband
        gender = "female"
    }

    fun checkConsistency(personById: Map<String?, Person>): Boolean {
        return id != null &&
                firstName != null &&
                lastName != null &&
                gender != null &&
                parents.size <= 2 &&
                childrenNumber != null &&
                siblingsNumber != null &&
                children.size == childrenNumber &&
                siblings.size == siblingsNumber &&
                (spouse != null || childrenNumber == 0) &&
                children.stream()
                    .map { obj: Person -> obj.parents }
                    .allMatch { s: Set<Person> ->
                        listOf<Any>(
                            *s.toTypedArray()
                        ).contains(this)
                    } &&
                parents.stream()
                    .map { obj: Person -> obj.children }
                    .allMatch { s: Set<Person> ->
                        listOf<Any>(
                            *s.toTypedArray()
                        ).contains(this)
                    } &&
                siblings.stream()
                    .map { obj: Person -> obj.siblings }
                    .allMatch { s: Set<Person> ->
                        listOf<Any>(
                            *s.toTypedArray()
                        ).contains(this)
                    } &&
                Stream.concat(
                    Stream.of<Set<Person>>(children, siblings, parents)
                        .flatMap { obj: Set<Person> -> obj.stream() },
                    Stream.ofNullable(spouse)
                ).allMatch { p: Person? -> p!!.id != null && personById[p.id] === p }
    }

    fun lightMerge(person: Person) {
        if (person === this) return
        if (id == null || person.id == null || id != person.id) {
            println(this)
            println(person)
            throw Error("Incorrect id")
        }
        if (person.firstName != null) {
            firstName = person.firstName
        }
        if (person.lastName != null) {
            lastName = person.lastName
        }
        if (person.gender != null) {
            gender = person.gender
        }
        if (person.childrenNumber != null) {
            childrenNumber = person.childrenNumber!!
        }
        if (person.siblingsNumber != null) {
            siblingsNumber = person.siblingsNumber!!
        }
    }

    fun mergePerson(person: Person) {
        lightMerge(person)
        if (person === this) return
        if (person.spouse != null) {
            spouse = person.spouse
            spouse!!.spouse = this
        }
        for (parent in person.parents) {
            parent.children.remove(person)
            parent.children.add(this)
        }
        for (child in person.children) {
            child.parents.remove(person)
            child.parents.add(this)
            children.add(child)
        }
        for (sibling in person.siblings) {
            sibling.siblings.remove(person)
            sibling.siblings.add(this)
            siblings.add(sibling)
        }
    }

    override fun toString(): String {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", childrenNumber=" + childrenNumber +
                ", siblingsNumber=" + siblingsNumber +
                '}'
    }

    fun toStringMain(): String {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", childrenNumber=" + childrenNumber +
                ", siblingsNumber=" + siblingsNumber +
                ", spouse=" + spouse +
                ", parents=" + parents.stream().toList() +
                ", siblings=" + siblings.stream().toList() +
                ", children=" + children.stream().toList() +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val person = other as Person
        return if (id != null && person.id != null && id == person.id) true else fullName != null && person.fullName != null && fullName == person.fullName
    }

    override fun hashCode(): Int {
        return Objects.hash(id, firstName, lastName, gender, childrenNumber, siblingsNumber)
    }
}