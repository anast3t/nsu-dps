//import javax.xml.bind.annotation.XmlAttribute
//import javax.xml.bind.annotation.XmlElement
//import javax.xml.bind.annotation.adapters.XmlAdapter
//
//class PersonXmlAdapter: XmlAdapter<PersonXmlAdapter.XmlPerson, Person>() {
//
//    class Ref(
//        @field:XmlAttribute
//        val id: String
//    )
//
//    class Spouse(
//        @field:XmlAttribute
//        val id: String
//    )
//
//    class Children(
//        private val daughtersIds: List<String>,
//        private val sonsIds: List<String>
//    ){
//        @XmlElement
//        val daughter: List<Ref> = daughtersIds.map {
//            Ref(it)
//        }.toList()
//
//        @XmlElement
//        val son: List<Ref> = sonsIds.map {
//            Ref(it)
//        }
//
//        @XmlAttribute
//        val childrenNumber: Int = daughtersIds.size + sonsIds.size
//    }
//
//    class Siblings(
//        private val brotherIds: List<String>,
//        private val sistersIds: List<String>
//    ) {
//        @XmlAttribute
//        val siblingsCount: Int = brotherIds.size + sistersIds.size
//
//        @XmlElement
//        val brother: List<Ref> = brotherIds.map {
//            Ref(it)
//        }
//
//        @XmlElement
//        val sister: List<Ref> = sistersIds.map {
//            Ref(it)
//        }
//    }
//
//    class Parents(
//        @field:XmlElement
//        val mother: Ref?,
//        @field:XmlElement
//        val father: Ref?
//    )
//
//    class XmlPerson(
//        @field:XmlAttribute
//        val id: String,
//        @field:XmlAttribute
//        val firstname: String?,
//        @field:XmlAttribute
//        val lastname: String?,
//        @field:XmlAttribute
//        val gender: String?,
//        @field:XmlElement
//        val spouse: Spouse,
//        @field:XmlElement
//        val children: Children,
//        @field:XmlElement
//        val siblings: Siblings,
//        @field:XmlElement
//        val parents: Parents
//    ) {
//
//    }
//
//    override fun unmarshal(v: XmlPerson?): Person {
//        TODO("Not yet implemented")
//    }
//
//    override fun marshal(v: Person?): XmlPerson {
//        if (v != null) {
//            return XmlPerson(
//                id = v.id,
//                firstname = v.firstName,
//                lastname = v.lastName,
//                gender = v.gender,
//                spouse = Spouse(v.spouse),
//                children = Children()
//            )
//        }
//    }
//}