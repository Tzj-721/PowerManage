<!--这里是echarts-->
<template>
  <div>
    <el-row><!--第一行-->
      <el-card class="box-card" style="margin-top: 0px">
        <!-- 统计图 -->
        <div ref="echarts1" style="height: 225px"></div>
      </el-card>
    </el-row>

    <el-row><!--第二行-->
      <!--左列 -->
      <el-col :span="12">
        <el-card class="box-card" style="margin-top: 5px">
          <!-- 左1 -->
          <div ref="echarts2" style="height: 230px"></div>
        </el-card>
        <el-card class="box-card" style="margin-top: 5px">
          <!-- 左2 -->
          <div ref="echarts3" style="height: 350px"></div>
        </el-card>
      </el-col>

      <!--右列 -->
      <el-col :span="12" style="padding-left: 20px">
        <el-card class="box-card" style="margin-top: 5px">
          <!-- 右1 -->
          <div ref="echarts4" style="height: 230px"></div>
        </el-card>
        <el-card class="box-card" style="margin-top: 5px">
          <!-- 右2 -->
          <div ref="echarts5" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row><!--第三行-->
      <el-card class="box-card" style="margin-top: 5px">
        <!-- 统计图 -->
        <div ref="echarts6" style="height: 350px"></div>
      </el-card>
    </el-row>

  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  name: 'myOrder',
  data() {
    return {}
  }, mounted() {   // 指定echarts图表的配置项和数据
    var mycharts1 = echarts.init(this.$refs.echarts1);
    function getVirtualData(year) {
      const date = +echarts.time.parse(year + '-01-01');
      const end = +echarts.time.parse(+year + 1 + '-01-01');
      const dayTime = 3600 * 24 * 1000;
      const data = [];
      for (let time = date; time < end; time += dayTime) {
        data.push([
          echarts.time.format(time, '{yyyy}-{MM}-{dd}', false),
          Math.floor(Math.random() * 10000)
        ]);
      }
      return data;
    }
    var option1 = {
      title: {
        top: 30,
        left: 'center',
        text: '全年的日销售数据'
      },
      tooltip: {},
      visualMap: {
        min: 0,
        max: 10000,
        type: 'piecewise',
        orient: 'horizontal',
        left: 'center',
        top: 65
      },
      calendar: {
        top: 120,
        left: 30,
        right: 30,
        cellSize: ['auto', 13],
        range: '2016',
        itemStyle: {
          borderWidth: 0.5
        },
        yearLabel: { show: false }
      },
      series: {
        type: 'heatmap',
        coordinateSystem: 'calendar',
        data: getVirtualData('2016')
      }
    };
    mycharts1.setOption(option1);

    var mycharts2 = echarts.init(this.$refs.echarts2);
    const colorList = [
      '#4f81bd',
      '#c0504d',
      '#9bbb59',
      '#604a7b',
      '#948a54',
      '#e46c0b'
    ];
    const data4 = [
      [10, 16, 3, 'A'],
      [16, 18, 15, 'B'],
      [18, 26, 12, 'C'],
      [26, 32, 22, 'D'],
      [32, 56, 7, 'E'],
      [56, 62, 17, 'F']
    ].map(function (item, index) {
      return {
        value: item,
        itemStyle: {
          color: colorList[index]
        }
      };
    });
    var option2 = {
      title: {
        text: '利润',
        left: 'center'
      },
      tooltip: {},
      xAxis: {
        scale: true
      },
      yAxis: {},
      series: [
        {
          type: 'custom',
          renderItem: function (params, api) {
            var yValue = api.value(2);
            var start = api.coord([api.value(0), yValue]);
            var size = api.size([api.value(1) - api.value(0), yValue]);
            var style = api.style();
            return {
              type: 'rect',
              shape: {
                x: start[0],
                y: start[1],
                width: size[0],
                height: size[1]
              },
              style: style
            };
          },
          label: {
            show: true,
            position: 'top'
          },
          dimensions: ['from', 'to', 'profit'],
          encode: {
            x: [0, 1],
            y: 2,
            tooltip: [0, 1, 2],
            itemName: 3
          },
          data: data4
        }
      ]
    };
    mycharts2.setOption(option2);

    var mycharts3 = echarts.init(this.$refs.echarts3);
    var option3 = {
      angleAxis: {
        max: 2,
        startAngle: 30,
        splitLine: {
          show: false
        }
      },
      radiusAxis: {
        type: 'category',
        data: ['v', 'w', 'x', 'y', 'z'],
        z: 10
      },
      polar: {},
      series: [
        {
          type: 'bar',
          data: [4, 3, 2, 1, 0],
          coordinateSystem: 'polar',
          name: 'Show with Blue',
          itemStyle: {
            borderColor: 'red',
            opacity: 0.8,
            borderWidth: 1
          }
        },
        {
          type: 'bar',
          data: [4, 3, 2, 1, 0],
          coordinateSystem: 'polar',
          name: 'Show with Green',
          roundCap: true,
          itemStyle: {
            borderColor: 'green',
            opacity: 0.8,
            borderWidth: 1
          }
        }
      ],
      legend: {
        show: true,
        data: ['Show with Blue', 'Show with Green']
      }
    };
    mycharts3.setOption(option3);

    var mycharts4 = echarts.init(this.$refs.echarts4);
    const categories = (function () {
      let now = new Date();
      let res = [];
      let len = 10;
      while (len--) {
        res.unshift(now.toLocaleTimeString().replace(/^\D*/, ''));
        now = new Date(+now - 2000);
      }
      return res;
    })();
    const categories2 = (function () {
      let res = [];
      let len = 10;
      while (len--) {
        res.push(10 - len - 1);
      }
      return res;
    })();
    const data = (function () {
      let res = [];
      let len = 10;
      while (len--) {
        res.push(Math.round(Math.random() * 1000));
      }
      return res;
    })();
    const data2 = (function () {
      let res = [];
      let len = 0;
      while (len < 10) {
        res.push(+(Math.random() * 10 + 5).toFixed(1));
        len++;
      }
      return res;
    })();
    var option4 = {
      title: {
        text: '实时销售数据'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#283b56'
          }
        }
      },
      legend: {},
      toolbox: {
        show: true,
        feature: {
          dataView: { readOnly: false },
          restore: {},
          saveAsImage: {}
        }
      },
      dataZoom: {
        show: false,
        start: 0,
        end: 100
      },
      xAxis: [
        {
          type: 'category',
          boundaryGap: true,
          data: categories
        },
        {
          type: 'category',
          boundaryGap: true,
          data: categories2
        }
      ],
      yAxis: [
        {
          type: 'value',
          scale: true,
          name: 'Price',
          max: 30,
          min: 0,
          boundaryGap: [0.2, 0.2]
        },
        {
          type: 'value',
          scale: true,
          name: 'Order',
          max: 1200,
          min: 0,
          boundaryGap: [0.2, 0.2]
        }
      ],
      series: [
        {
          name: 'Actually',
          type: 'bar',
          xAxisIndex: 1,
          yAxisIndex: 1,
          data: data
        },
        {
          name: 'Expected',
          type: 'line',
          data: data2
        }
      ]
    };
    app.count = 11;
    setInterval(function () {
      let axisData = new Date().toLocaleTimeString().replace(/^\D*/, '');
      data.shift();
      data.push(Math.round(Math.random() * 1000));
      data2.shift();
      data2.push(+(Math.random() * 10 + 5).toFixed(1));
      categories.shift();
      categories.push(axisData);
      categories2.shift();
      categories2.push(app.count++);
      myChart.setOption({
        xAxis: [
          {
            data: categories
          },
          {
            data: categories2
          }
        ],
        series: [
          {
            data: data
          },
          {
            data: data2
          }
        ]
      });
    }, 2100);
    mycharts4.setOption(option4);

    var mycharts5 = echarts.init(this.$refs.echarts5);
    var option5 = {
      color: ['#80FFA5', '#00DDFF', '#37A2FF', '#FF0087', '#FFBF00'],
      title: {
        text: '商品进货量'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        }
      },
      legend: {
        data: ['Type 1', 'Type 2', 'Type 3', 'Type 4', 'Type 5']
      },
      toolbox: {
        feature: {
          saveAsImage: {}
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          boundaryGap: false,
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          name: 'Type 1',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0
          },
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(128, 255, 165)'
              },
              {
                offset: 1,
                color: 'rgb(1, 191, 236)'
              }
            ])
          },
          emphasis: {
            focus: 'series'
          },
          data: [140, 232, 101, 264, 90, 340, 250]
        },
        {
          name: 'Type 2',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0
          },
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(0, 221, 255)'
              },
              {
                offset: 1,
                color: 'rgb(77, 119, 255)'
              }
            ])
          },
          emphasis: {
            focus: 'series'
          },
          data: [120, 282, 111, 234, 220, 340, 310]
        },
        {
          name: 'Type 3',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0
          },
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(55, 162, 255)'
              },
              {
                offset: 1,
                color: 'rgb(116, 21, 219)'
              }
            ])
          },
          emphasis: {
            focus: 'series'
          },
          data: [320, 132, 201, 334, 190, 130, 220]
        },
        {
          name: 'Type 4',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0
          },
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(255, 0, 135)'
              },
              {
                offset: 1,
                color: 'rgb(135, 0, 157)'
              }
            ])
          },
          emphasis: {
            focus: 'series'
          },
          data: [220, 402, 231, 134, 190, 230, 120]
        },
        {
          name: 'Type 5',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0
          },
          showSymbol: false,
          label: {
            show: true,
            position: 'top'
          },
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(255, 191, 0)'
              },
              {
                offset: 1,
                color: 'rgb(224, 62, 76)'
              }
            ])
          },
          emphasis: {
            focus: 'series'
          },
          data: [220, 302, 181, 234, 210, 290, 150]
        }
      ]
    };
    mycharts5.setOption(option5);

    var mycharts6 = echarts.init(this.$refs.echarts6);
    var option6 = {
      color: ['#67F9D8', '#FFE434', '#56A3F1', '#FF917C'],
      title: {
        text: '商品使用星形图'
      },
      legend: {},
      radar: [
        {
          indicator: [
            { text: '寿命' },
            { text: '可靠性' },
            { text: '安全性' },
            { text: '适应性' },
            { text: '经济性' }
          ],
          center: ['25%', '50%'],
          radius: 120,
          startAngle: 90,
          splitNumber: 4,
          shape: 'circle',
          axisName: {
            formatter: '【{value}】',
            color: '#428BD4'
          },
          splitArea: {
            areaStyle: {
              color: ['#77EADF', '#26C3BE', '#64AFE9', '#428BD4'],
              shadowColor: 'rgba(0, 0, 0, 0.2)',
              shadowBlur: 10
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(211, 253, 250, 0.8)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(211, 253, 250, 0.8)'
            }
          }
        },
        {
          indicator: [
            { text: '寿命', max: 150 },
            { text: '可靠性', max: 150 },
            { text: '安全性', max: 150 },
            { text: '适应性', max: 120 },
            { text: '经济性', max: 108 },
            { text: '性能', max: 72 }
          ],
          center: ['75%', '50%'],
          radius: 120,
          axisName: {
            color: '#fff',
            backgroundColor: '#666',
            borderRadius: 3,
            padding: [3, 5]
          }
        }
      ],
      series: [
        {
          type: 'radar',
          emphasis: {
            lineStyle: {
              width: 4
            }
          },
          data: [
            {
              value: [100, 8, 0.4, -80, 2000],
              name: '商品 A'
            },
            {
              value: [60, 5, 0.3, -100, 1500],
              name: '商品 B',
              areaStyle: {
                color: 'rgba(255, 228, 52, 0.6)'
              }
            }
          ]
        },
        {
          type: 'radar',
          radarIndex: 1,
          data: [
            {
              value: [120, 118, 130, 100, 99, 70],
              name: '商品 C',
              symbol: 'rect',
              symbolSize: 12,
              lineStyle: {
                type: 'dashed'
              },
              label: {
                show: true,
                formatter: function (params) {
                  return params.value;
                }
              }
            },
            {
              value: [100, 93, 50, 90, 70, 60],
              name: '商品 D',
              areaStyle: {
                color: new echarts.graphic.RadialGradient(0.1, 0.6, 1, [
                  {
                    color: 'rgba(255, 145, 124, 0.1)',
                    offset: 0
                  },
                  {
                    color: 'rgba(255, 145, 124, 0.9)',
                    offset: 1
                  }
                ])
              }
            }
          ]
        }
      ]
    };
    mycharts6.setOption(option6);

  },
}
</script>

<style lang = "less" scoped></style>
