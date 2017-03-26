package models

import scalaj.http._
import play.api.libs.json._

object HeroData { implicit val owDataJsonFormat: Format[HeroData] = Json.format[HeroData]}

case class HeroData(  id: Int,
                      name: String,
                      description: String,
                      health: Option[Int],
                      armour: Option[Int],
                      shield: Option[Int],
                      real_name: Option[String],
                      age: Option[Int],
                      height: Option[Int],
                      affiliation: Option[String],
                      base_of_operations: Option[String],
                      difficulty: Option[Int],
                      url: Option[String],
                      var stats: Option[Map[String, String]] )


object OverwatchData {

  val apiUrl = "https://overwatch-api.net/api/v1/hero"

  def getJson (url: String): JsValue = { Json.parse(Http(url).asString.body) }

  //def getDataList [T](element: String): List[T] = {(getJson(apiUrl) \ element).as[List[JsObject]].map( data => Json.fromJson[T](data).get ) }

  def getHeroDataList: List[HeroData] = { (getJson(apiUrl) \ "data").as[List[JsObject]].map( data => Json.fromJson[HeroData](data).get )}

  def updateHeroStats(hero: HeroData, stats: Map[String, String]): HeroData ={
    hero.stats = Option(stats)
    hero
  }


}
