package models

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import org.apache.commons.lang3.StringUtils


object OverwatchScraper {

  private val browser = JsoupBrowser()
  private val websites: List[OverwatchWebsite] = initWebsites()

  /**
    * Get heroes overall data by platform
    * @return [platform] -> [hero.name -> HeroData]
    */
  def getHeroesOverallData: Map[String, Map[String, HeroData]] = {
    val website = getWebsite("OverBuff")
    website.platform.map(platform => platform -> scrapeHeroesStats( website, platform )).toMap
  }


  private def scrapeHeroStats(doc: Document): Map[String, String] ={ getElementList(doc, ".stat").map(_ >> (text(".label"), text(".value"))).toMap }

  private def scrapeHeroesStats(website: OverwatchWebsite, platform: String): Map[String, HeroData] = {
    OverwatchData.getHeroDataList.map(hero => hero.name -> {
      val doc = getWebsiteDocument(website, platform, hero.name)
      val stats = scrapeHeroStats(doc)
      OverwatchData.updateHeroStats(hero, stats)
    }).toMap
  }


  private def getWebsite(name: String): OverwatchWebsite ={ websites.find(o=> o.name == name).get }
  private def getElementList(doc: Document, query: String): List[Element] = { doc >> elementList(query) }
  private def getWebsiteDocument(website: OverwatchWebsite, platform: String, hero: String): Document ={
    val normalize = (s: String) => StringUtils.stripAccents(s.filter(c=> c.isLetterOrDigit))
    browser.get(website.getUrlWithParams(platform, normalize(hero)))
  }

  private def initWebsites(): List[OverwatchWebsite] ={
    List(
      new OverwatchWebsite{
        name = "OverwatchTracker"
        url = "https://overwatchtracker.com/leaderboards/{platform}/global/EliminationsPM/hero/{hero}?page=1&mode=1"
        platform = List("pc", "xbox", "psn")
        container = "table"
        table_value = ".pull-right"
      },
      new OverwatchWebsite{
        name = "MasterOverwatch"
        url = "http://masteroverwatch.com/leaderboards/{platform}/global/hero/23/mode/ranked/category/averageeliminations"
        platform = List("pc", "xbl", "psn")
        container = ".table-body"
        table_value = ".table-main-value strong"
      },
      new OverwatchWebsite{
        name = "OverBuff"
        url = "https://www.overbuff.com/heroes/{hero}?mode=competitive&platform={platform}"
        platform = List("pc", "xbl", "psn")
        container = ".player-hero"
        table_value = ".value"
      }
    )
  }

  private class OverwatchWebsite {
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
}
