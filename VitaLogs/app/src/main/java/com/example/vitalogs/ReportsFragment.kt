package com.example.vitalogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout para este fragmento
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Encontra os gráficos no layout
        val caloriesChart = view.findViewById<BarChart>(R.id.caloriesChart)
        val macrosChart = view.findViewById<PieChart>(R.id.macrosChart)

        // Configura e preenche os gráficos com dados de exemplo
        setupCaloriesChart(caloriesChart)
        setupMacrosChart(macrosChart)
    }

    private fun setupCaloriesChart(chart: BarChart) {
        // Dados de exemplo
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 1900f)) // Seg
        entries.add(BarEntry(1f, 2200f)) // Ter
        entries.add(BarEntry(2f, 2050f)) // Qua
        entries.add(BarEntry(3f, 2300f)) // Qui
        entries.add(BarEntry(4f, 1800f)) // Sex
        entries.add(BarEntry(5f, 2500f)) // Sáb
        entries.add(BarEntry(6f, 2100f)) // Dom

        val dataSet = BarDataSet(entries, "Calorias Consumidas")
        dataSet.color = Color.parseColor("#16DEAD") // Cor verde do seu tema
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        chart.data = barData

        // Estilização do gráfico
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.setFitBars(true)
        chart.invalidate() // Desenha o gráfico
    }

    private fun setupMacrosChart(chart: PieChart) {
        // Dados de exemplo (em porcentagem)
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(50f, "Carboidratos"))
        entries.add(PieEntry(30f, "Proteínas"))
        entries.add(PieEntry(20f, "Gorduras"))

        val dataSet = PieDataSet(entries, "Distribuição de Macros")

        // Cores para cada fatia
        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#FBBF24")) // Amarelo
        colors.add(Color.parseColor("#34D399")) // Verde
        colors.add(Color.parseColor("#FB923C")) // Laranja
        dataSet.colors = colors

        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()}%"
            }
        }

        val pieData = PieData(dataSet)
        chart.data = pieData

        // Estilização do gráfico
        chart.description.isEnabled = false
        chart.isDrawHoleEnabled = true // Para efeito "doughnut"
        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f
        chart.legend.textSize = 12f
        chart.invalidate() // Desenha o gráfico
    }
}