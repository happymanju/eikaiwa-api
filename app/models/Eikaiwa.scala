package models

import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.distribution.PoissonDistribution
import org.apache.commons.math3.stat.descriptive.rank.Percentile
import play.api.libs.json.JsValue

object Eikaiwa {
  def simInstructor(
      avgWeekday: Double,
      stdvWeekday: Double,
      avgSat: Double,
      stdvSat: Double,
      avgSun: Double,
      stdvSun: Double,
      weekDays: Int,
      satDays: Int,
      sunDays: Int
  ): Double = {
    val weekdayDist: NormalDistribution =
      new NormalDistribution(avgWeekday, stdvWeekday)

    val satDist: NormalDistribution = new NormalDistribution(avgSat, stdvSat)
    val sunDist: NormalDistribution = new NormalDistribution(avgSun, stdvSun)

    val weekdaySamples: List[Double] = List.fill(weekDays)(weekdayDist.sample())
    val satSamples: List[Double] = List.fill(satDays)(satDist.sample())
    val sunSamples: List[Double] = List.fill(sunDays)(sunDist.sample())

    //sum lists individually and then to each other
    val total = weekdaySamples.reduce(_ + _) + satSamples.reduce(
      _ + _
    ) + sunSamples.reduce(
      _ + _
    )
    return total
  }

  def oneMonthStudio(data: JsValue): Int = {
    val stats = data.as[Map[String, Double]]
    //setup distributions
    val monDist = new NormalDistribution(stats("avgMon"), stats("stdvMon"))
    val tueDist = new NormalDistribution(stats("avgTue"), stats("stdvTue"))
    val wedDist = new NormalDistribution(stats("avgWed"), stats("stdvWed"))
    val thuDist = new NormalDistribution(stats("avgThu"), stats("stdvThu"))
    val friDist = new NormalDistribution(stats("avgFri"), stats("stdvFri"))
    val satDist = new NormalDistribution(stats("avgSat"), stats("stdvSat"))
    val sunDist = new NormalDistribution(stats("avgSun"), stats("stdvSun"))

    //run sim
    val oneMonthSamplesDist = Array.fill(10000)(
      monDist.sample + tueDist.sample + wedDist.sample + thuDist.sample + friDist.sample + satDist.sample + sunDist.sample
    )

    val percentile =
      new Percentile().evaluate(oneMonthSamplesDist, 25).round.toInt

    return percentile

  }
}
