package models


class OverwatchWebsite {
  var name: String = ""
  var url: String = ""
  var platform: List[String] = List[String]()
  var container: String = ""
  var table_value: String = ""

  def getUrlWithParams(platform: String, hero: String): String = {
    var urlWithParams = url
    urlWithParams
      .replace("{platform}", platform)
      .replace("{hero}", hero)
  }
}

/*
def scrape(): Unit ={
  websites.foreach(
    website=>{
      print(website.name)
      println("----------------------------------")
      val doc = browser.get(website.getUrlWithParams("pc", "fg"))
      val table: Element = doc >> element(website.container)
      val list : List[Element] = table >> elementList(website.table_value)
      list.foreach(
        l => { println("Elim " + l.text.filter(c=> c.isDigit || c.equals('.'))) }
      )
    }
  )
}
*/