package models

import play.api.libs.json.{Format, Json}

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

object HeroData { implicit val owDataJsonFormat: Format[HeroData] = Json.format[HeroData]}
