package helpers

import models.OverwatchWebsite


object Settings {

  val overwatchHeroesApi = "https://overwatch-api.net/api/v1/hero"

  val websites : List[OverwatchWebsite] = List(
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
