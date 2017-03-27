package helpers

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import org.apache.commons.lang3.StringUtils
import play.api.libs.json.{JsObject, JsValue, Json}
import scala.collection.parallel.immutable.ParMap
import scalaj.http.Http

import models.{HeroData, OverwatchWebsite}


object Scraper {

  private val browser = JsoupBrowser()
  private val websites: List[OverwatchWebsite] = Settings.websites

  /**
    * Get heroes overall data by platform
    * @return [platform] -> [hero.name -> HeroData]
    */
  def getHeroesOverallData: Map[String, ParMap[String, HeroData]] = {
    val website = getWebsite("OverBuff")
    website.platform.map(platform => platform -> Scraper.scrapeHeroesStats( website, platform )).toMap
  }

  /**
    * Get hero overall data by platform
    * @param hero name
    * @return [platform] -> [HeroData]
    */
  def getHeroOverallData(hero: String): Map[String, HeroData] = {
    val website = getWebsite("OverBuff")
    website.platform.map(platform => platform -> {
      val doc = getWebsiteDocument(website, platform, hero)
      updateHeroStats(getHeroData(hero), scrapeHeroStats(doc))
    }).toMap
  }

  //* Utils

  private def normalize = (s: String) => StringUtils.stripAccents(s.filter(c=> c.isLetterOrDigit).toLowerCase)
  private def getWebsite(name: String): OverwatchWebsite ={ websites.find(o=> o.name == name).get }
  private def getElementList(doc: Document, query: String): List[Element] = { doc >> elementList(query) }
  private def getWebsiteDocument(website: OverwatchWebsite, platform: String, hero: String): Document ={
    browser.get(website.getUrlWithParams(platform, normalize(hero)))
  }

  //* Retrieve Hero data with API

  private def getJson (url: String): JsValue = { Json.parse(Http(url).asString.body) }
  private def getHeroData(hero: String): HeroData = { getHeroesDataList.find(h=> normalize(h.name).contains(normalize(hero))).get}
  private def getHeroesDataList: List[HeroData] = { (getJson(Settings.overwatchHeroesApi) \ "data").as[List[JsObject]].map( data => Json.fromJson[HeroData](data).get )}
  private def updateHeroStats(hero: HeroData, stats: Map[String, String]): HeroData = {
    hero.stats = Option(stats)
    hero
  }

  //* Scrape websites

  private def scrapeHeroStats(doc: Document): Map[String, String] ={ getElementList(doc, ".stat").map(_ >> (text(".label"), text(".value"))).toMap }
  private def scrapeHeroesStats(website: OverwatchWebsite, platform: String): ParMap[String, HeroData] = {
    getHeroesDataList.par.map(hero => hero.name -> {
      val doc = getWebsiteDocument(website, platform, hero.name)
      val stats = scrapeHeroStats(doc)
      updateHeroStats(hero, stats)
    }).toMap
  }
}
