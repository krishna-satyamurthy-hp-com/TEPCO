<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="C1082-Car">
 <div class="contents__main mb20">

        <section class="main__block">
          <div>
            <h1 class="c-hdg__page mb60">光熱費シミュレーション</h1>
            <div>
              <h2 class="c-hdg__section">光熱費シミュレーション Lite</h2>
              <p class="mb50">暮らしの中で毎日使うエネルギー。オール電化リフォームや電気自動車の導入で、快適でおトクなくらしをはじめませんか？</p>
            </div>
          </div>
        </section>

        <div class="p-lifesim-tab__pagelink">
          <ul class="p-list-tab__btn c-grid">
            <li class="c-grid__item col--1of2">
			<a><xsl:attribute name="href">
			<xsl:value-of select="/Properties/Data/Datum[@Name='tab-1']"/>
			</xsl:attribute>ォームシミュレーション</a></li>
            <li class="c-grid__item col--1of2 is-active">
              <a><xsl:attribute name="href">
			<xsl:value-of select="/Properties/Data/Datum[@Name='tab-2']"/>
			</xsl:attribute>電気自動車導入シミュレーション
              </a>
            </li>
          </ul>
        </div>

        <section class="main__block">

          <div class="pl50">
            <p class="mb60">ガソリン車等から電気自動車に買い替えた場合の燃料費（ランニングコスト）の目安がわかります。</p>

            <h3 class="p-lifesim-hdg__section-sub mb50">電気自動車導入シミュレーション</h3>

            <div class="js-box__simulator">
              <div class="p-lifesim-box__selecter mb60">
                <div class="p-lifesim-grid--image">
                  <p><img src="/assets/pc/images/life/sim/index-a/pic-01.png" alt="" /></p>
                </div>
                <div class="p-lifesim-grid--contents">
                    <p class="p-lifesim-text__question">
                      <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/faq1" />
                    </p>
                    <div class="p-lifesim-grid--middle mb20">
                      <select class="js-list__data--select--q1">
                        <option value="1" selected="selected">5 km/L</option>
                        <option value="2">10 km/L</option>
                        <option value="3">15 km/L</option>
                        <option value="4">20 km/L</option>
                        <option value="5">25 km/L</option>
                        <option value="6">30 km/L</option>
                      </select>
                    </div>

                    <p class="p-lifesim-text__question">
                      <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/faq2" />
                    </p>
                    <div class="p-lifesim-grid--middle">
                      <select class="js-list__data--select--q2">
                        <option value="1" selected="selected">2,000 km/年 程度</option>
                        <option value="2">5,000 km/年 程度</option>
                        <option value="3">10,000 km/年 程度</option>
                        <option value="4">15,000 km/年 程度</option>
                        <option value="5">20,000 km/年 程度</option>
                      </select>
                    </div>
                </div>
              </div>


            <div class="p-lifesim-box__result mb50">
              <h4 class="p-lifesim-hdg__section-sub mb20">年間の燃料費（ランニングコスト）の比較</h4>

              <div class="p-lifesim-box__image mb50">
                <h4 class="p-lifesim-hdg__section-sub u-ta--c mb15">電気自動車の導入による燃料費（ランニングコスト）の変化</h4>
                <p class="u-ta--c mb40">
				<img src="/assets/pc/images/life/sim/index-a/graph-1-1.png" alt="" class="js-image__result" /></p>
                <ul class="p-lifesim-list__notice">
                  <li>※グラフの記載の金額はすべて1,000円未満で四捨五入した概算値です。</li>
                  <li>※グラフに記載の金額は試算値であり、実際の金額は、お客さまの走行環境や運転方法、気象状況、燃料価格、原料価格等の変動に応じて変動しますので、ご留意ください。また、外出先で充電を行う場合は、料金体系が異なりますのでご留意ください。</li>
                  <li>※グラフに記載の金額にはリフォームにおける機器導入時に必要となる初期費用（自動車本体価格、充電設備本体価格・設備工事費等）を含んでおりません。</li>
                  <li>※グラフに記載の電気料金には、基本料金、再生可能エネルギー発電促進賦課金、燃料費調整額を含んでおりません。また、ご自宅の契約電力変更による基本料金の変動を加味しておりません。</li>
                  <li>※上記は燃料費（ランニングコスト）の比較であり、車そのものの特徴（航続距離、電池の経年劣化等）は考慮しておりません。</li>
                </ul>
              </div>

              <div class="js-accordion">

                <p><a class="c-btn c-btn--secondary js-accordion-button">試算条件</a></p>

                <div class="p-lifesim-box__about js-accordion-contents mt20">
                  <p class="p-lifesim-text__title">試算条件について</p>
                  <p>
                    ＜ガソリン車＞<br />
                    ガソリン料金：138.6円/L（（一財）日本エネルギー経済研究所　石油情報センター 2015年4月6日調査日分関東局）<br />
                    ＜EV＞<br />
                    ・JC08モード交流電力量消費率：114Wh/km（日産自動車・リーフの主要緒元）<br />
                    ・充電効率：90%<br />
                    ・電力量料金単価<br />
                    <span class="pl10">25.91円/kWh（東京電力「従量電灯」（2014年3月1日実施）の第2段階料金単価）</span><br />
                    <span class="pl10">12.16円/kWh（東京電力「電化上手（季節別時間帯別電灯）」（2014年3月1日実施）の夜間時間帯単価）</span><br />
                    ※電気料金には、基本料金、再生可能エネルギー発電促進賦課金、燃料費調整額を含んでおりません。<br />
                    ※車種ごとの燃費は自動車メーカーへお問い合わせください。
                  </p>
                </div>
              </div>

            </div>
          </div>
        </div>

        <dl class="c-accordion mb30">
          <dt class="c-accordion__title">
            <span>おすすめのポイント</span>
            <a href="javascript:void(0);" class="c-accordion__switch c-btn-icon icon-a1_btm"></a>
          </dt>
          <dd class="c-accordion__body">
            <section>
              <div class="c-grid c-grid--gutter-20 pr35 pl35 pt30">
                <div class="c-grid__item col--1of2">
                  <p class="p-lifesim-text__catch mb25">静かにエコに、街にもお財布にも優しく。</p>

                  <ul class="p-lifesim-list__bullet mb20">
                    <li>燃費性能が高く、環境に優しい車。</li>
                    <li>家庭でも外出先でも簡単に充電できる。</li>
                    <li>夜間割安な電気料金メニューで夜間充電すれば、さらに経済的。</li>
                  </ul>
                  <p><a class="c-btn c-btn--primary">詳細はこちら</a></p>
                </div>
                <div class="c-grid__item col--1of2 u-ta--r">
                  <div class="c-grid c-grid--gutter-20">
                    <div class="c-grid__item col--1of2">
                      <p><img src="/assets/pc/images/life/sim/index-a/pic-03.png" alt="" /></p>
                    </div>
                    <div class="c-grid__item col--1of2">
                      <p><img src="/assets/pc/images/life/sim/index-a/pic-04.png" alt="" /></p>
                    </div>
                  </div>
                </div>
              </div>
            </section>
          </dd>
        </dl>

        <dl class="c-accordion mb60">
          <dt class="c-accordion__title">
            <span>電気自動車メーカー一覧</span>
            <a href="javascript:void(0);" class="c-accordion__switch c-btn-icon icon-a1_btm"></a>
          </dt>
          <dd class="c-accordion__body">
            <section>
              <ul class="c-grid c-grid--gutter-20 pl35 pr35 pt30 p-lifesim-list__maker">
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg1/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker1" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg2/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker2" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg3/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker3" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg4/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker4" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg5/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker5" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg6/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker6" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg7/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker7" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg8/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker8" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg9/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker9" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg10/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker10" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg11/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker11" />
                  </p>
                </li>
                <li class="c-grid__item col--1of4">
                  <p class="mb15">
                    <img>
                      <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarimg12/img/@src" /></xsl:attribute>
                    </img>
                  </p>
                  <p class="u-ta--c">
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-car-content']/DCR/page/contents/eleccarmaker12" />
                  </p>
                </li>
              </ul>
            </section>
          </dd>
        </dl>

        <h2 class="c-hdg__section">各種お問い合わせ</h2>

        <ul class="c-grid c-grid--gutter-30 pr35 pl35 p-lifesim-list__btns">
          <li class="c-grid__item col--1of3"><a class="c-btn c-btn--secondary">よくあるお問い合わせ<br />(電化関連QA)</a></li>
          <li class="c-grid__item col--1of3"><a class="c-btn c-btn--secondary">簡易な機器取替やその他の<br />お問い合わせ</a></li>
        </ul>

      </section>
    </div>
	
	
	
	</xsl:template>
	
	<xsl:template name="C1082">
		 <div class="contents__main mb20">

        <section class="main__block">
          <div>
            <h1 class="c-hdg__page mb60">光熱費シュミレーション</h1>
            <div>
              <h2 class="c-hdg__section">光熱費シュミレーション Lite</h2>
              <p class="mb50">暮らしの中で毎日使うエネルギー。オール電化リフォームや電気自動車の導入で、快適でおトクなくらしをはじめませんか？</p>
            </div>
          </div>
        </section>

        <div class="p-lifesim-tab__pagelink">
          <ul class="p-list-tab__btn c-grid">
            <li class="c-grid__item col--1of2 is-active">
			<a>
				<xsl:attribute name="href">
				<xsl:value-of select="/Properties/Data/Datum[@Name='tab-1']"/>
				</xsl:attribute>オール電化リフォームシミュレーション
			</a>
			</li>
            <li class="c-grid__item col--1of2">
			<a><xsl:attribute name="href">
			<xsl:value-of select="/Properties/Data/Datum[@Name='tab-2']"/>
			</xsl:attribute>電気自動車導入シミュレーション</a></li>
          </ul>
        </div>

        <section class="main__block">
          <div class="pl50">
            <p class="mb60">ご家庭のキッチンや給湯をガスや灯油からすべて電気でまかなうオール電化にリフォームした場合の光熱費の目安がわかります。</p>

            <div class="js-box__simulator">
              <h3 class="p-lifesim-hdg__section-sub mb50">オール電化リフォームシュミレーション</h3>
              <div class="p-lifesim-box__selecter mb60">
                <div class="p-lifesim-grid--image">
                  <p><img src="/assets/pc/images/life/sim/index/pic-01.png" alt="" /></p>

                </div>
                <div class="p-lifesim-grid--contents">
                    <p class="p-lifesim-text__question">
                      <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/faq1" />
                    </p>
                    <ul class="p-lifesim-list_toggle">
                      <li><a class="c-btn c-btn--subtle is-select">契約アンペア</a></li>
                      <li><a class="c-btn c-btn--subtle">電気代・使用料</a></li>
                    </ul>

                    <ul class="p-lifesim-list_data--grid">
                      <li class="p-lifesim-grid--small js-list__data--select--q1">
                        <select>
                          <xsl:for-each select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/contcapa/option">
                            <option value="{@value}">
                              <xsl:if test="@selected='true'">
                                <xsl:attribute name="selected">selected</xsl:attribute>
                              </xsl:if>
                              <xsl:value-of select="." />
                            </option>
                          </xsl:for-each>
                        </select>
                        <p class="is-disable is-select"></p>
                      </li>
                      <li class="p-lifesim-grid--large js-list__data--select--q1">
                        <select>
                          <xsl:for-each select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/elecrateamused/option">
                            <option value="{@value}">
                              <xsl:if test="@selected='true'">
                                <xsl:attribute name="selected">selected</xsl:attribute>
                              </xsl:if>
                              <xsl:value-of select="." />
                            </option>
                          </xsl:for-each>
                        </select>
                        <p class="is-disable"></p>
                      </li>
                    </ul>

                    <p class="p-lifesim-text__question">
                      <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/faq2" />
                    </p>
                    <div class="p-lifesim-grid--middle">
                      <select class="js-list__data--select--q2">
                        <option value="" selected="selected">月々1,000円程度</option>
                        <option value="">月々3,000円程度</option>
                        <option value="">月々4,000円程度</option>
                        <option value="">月々5,000円程度</option>
                        <option value="">月々6,000円程度</option>
                        <option value="">月々7,000円程度</option>
                        <option value="">月々8,000円程度</option>
                        <option value="">月々9,000円程度</option>
                        <option value="">月々10,000円程度</option>
                        <option value="">月々11,000円程度</option>
                        <option value="">月々12,000円程度</option>
                        <option value="">月々13,000円程度</option>
                        <option value="">月々14,000円程度</option>
                      </select>
                    </div>
                </div>
              </div>



            <div class="p-lifesim-box__result mb50">
              <h4 class="p-lifesim-hdg__section-sub mb20">年間光熱費の比較</h4>

              <div class="p-lifesim-box__image mb50">
                <h4 class="p-lifesim-hdg__section-sub u-ta--c mb15">電化リフォームによる年間光熱費の変化</h4>
                <p class="u-ta--c mb40">
					<img src="/assets/pc/images/life/sim/index/graph-1-1.png" alt="" class="js-image__result" /></p>
                <ul class="p-lifesim-list__notice">
                  <li>※グラフの記載の金額はすべて1,000円未満で四捨五入した概算値です。</li>
                  <li>※グラフに記載の金額は試算値であり、実際の金額は、お客さまのご使用状況や、気象状況、燃料価格、原料価格等の変動に応じて変動します。またお客さまの住宅条件（地、広さ、断熱性能等）および住宅設備の状況、電気契約、ガスの種類および契約等によっても変動しますので、ご留意ください。</li>
                  <li>※グラフに記載の金額にはリフォームにおける機器導入時に必要となる初期費用（機器購入費や設備工事費等）を含んでおりません。</li>
                </ul>
              </div>

              <div class="js-accordion">

                <p><a class="c-btn c-btn--secondary js-accordion-button">試算条件</a></p>

                <div class="p-lifesim-box__about js-accordion-contents mt20">
                  <p class="p-lifesim-text__title">試算条件について</p>
                  <p>
                    <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/trialcalmsg" disable-output-escaping="yes" />
                  </p>
                </div>
              </div>

            </div>
          </div>


          </div>

          <h2 class="c-hdg__section">おすすめのポイント</h2>

          <dl class="c-accordion mb30">
            <dt class="c-accordion__title">
              <span>IH クッキングヒーター</span>
              <a href="javascript:void(0);" class="c-accordion__switch c-btn-icon icon-a1_btm"></a>
            </dt>
            <dd class="c-accordion__body">
              <section>
                <div class="c-grid c-grid--gutter-20 pr35 pl35 mb30 pt30">
                  <div class="c-grid__item col--1of2">
                    <p class="p-lifesim-text__catch mb25">
                      <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/disheasyenjoyreal" />
                    </p>

                    <ul class="p-lifesim-list__bullet mb20">
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishmsg1" />
                      </li>
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishmsg2" />
                      </li>
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishmsg3" />
                      </li>
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishmsg4" />
                      </li>
                    </ul>
                    <p><a class="c-btn c-btn--primary">詳細はこちら</a></p>
                  </div>
                  <div class="c-grid__item col--1of2 u-ta--r">
                    <div class="c-grid c-grid--gutter-20">
                      <div class="c-grid__item col--1of2">
                        <p>
                          <img>
                            <xsl:attribute name="src">
								<xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishimg1/img/@src" />
							</xsl:attribute>
                            <xsl:attribute name="width">
								<xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishimg1/img/@width" />
							</xsl:attribute>
                            <xsl:attribute name="height">
								<xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishimg1/img/@height" />
							</xsl:attribute>
                          </img>
                        </p>
                      </div>
                      <div class="c-grid__item col--1of2">
                        <p>
                          <img>
                            <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishimg2/img/@src" /></xsl:attribute>
                            <xsl:attribute name="width"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishimg2/img/@width" /></xsl:attribute>
                            <xsl:attribute name="height"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/dishimg2/img/@height" /></xsl:attribute>
                          </img>
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </section>
            </dd>
          </dl>

          <dl class="c-accordion mb30">
            <dt class="c-accordion__title">
              <span>エコキュート</span>
              <a href="javascript:void(0);" class="c-accordion__switch c-btn-icon icon-a1_btm"></a>
            </dt>
            <dd class="c-accordion__body">
              <section>
                <div class="c-grid c-grid--gutter-20 pr35 pl35 pt30">
                  <div class="c-grid__item col--1of2">
                    <p class="p-lifesim-text__catch mb25">
                      <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/waterspiritsynchwarm" />
                    </p>

                    <ul class="p-lifesim-list__bullet mb20">
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/ecocutemsg1" />
                      </li>
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/ecocutemsg2" />
                      </li>
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/ecocutemsg3" />
                      </li>
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/ecocutemsg4" />
                      </li>
                    </ul>
                    <p><a class="c-btn c-btn--primary">詳細はこちら</a></p>
                  </div>
                  <div class="c-grid__item col--1of2 u-ta--r">
                    <div class="c-grid c-grid--gutter-20">
                      <div class="c-grid__item col--1of2">
                        <p>
                          <img>
                            <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/ecocuteimg1/img/@src" /></xsl:attribute>
                          </img>
                        </p>
                      </div>
                      <div class="c-grid__item col--1of2">
                        <p>
                          <img>
                            <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/ecocuteimg2/img/@src" /></xsl:attribute>
                          </img>
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </section>
            </dd>
          </dl>

          <dl class="c-accordion mb60">
            <dt class="c-accordion__title">
              <span>省エネエアコン、ヒートポンプ温水式床暖房</span>
              <a href="javascript:void(0);" class="c-accordion__switch c-btn-icon icon-a1_btm"></a>
            </dt>
            <dd class="c-accordion__body">
              <section>
                <div class="c-grid c-grid--gutter-20 pr35 pl35 pt30">
                  <div class="c-grid__item col--1of2">
                    <p class="p-lifesim-text__catch mb25">
                      <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/clearairyou" />
                    </p>

                    <ul class="p-lifesim-list__bullet mb20">
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/clearmsg1" />
                      </li>
                      <li>
                        <xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/clearmsg2" />
                      </li>
                    </ul>
                    <p><a class="c-btn c-btn--primary">詳細はこちら</a></p>
                  </div>
                  <div class="c-grid__item col--1of2 u-ta--r">
                    <div class="c-grid c-grid--gutter-20">
                      <div class="c-grid__item col--1of2">
                        <p>
                          <img>
                            <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/savimg1/img/@src" /></xsl:attribute>
                          </img>
                        </p>
                      </div>
                      <div class="c-grid__item col--1of2">
                        <p>
                          <img>
                            <xsl:attribute name="src"><xsl:value-of select="/Properties/Data/Datum[@Name='sim-content']/DCR/page/contents/savimg2/img/@src" /></xsl:attribute>
                          </img>
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </section>
            </dd>
          </dl>

          <h2 class="c-hdg__section">各種お問い合わせ</h2>

          <ul class="c-grid c-grid--gutter-30 pr35 pl35 p-lifesim-list__btns">
            <li class="c-grid__item col--1of3"><a class="c-btn c-btn--secondary">よくあるお問い合わせ<br />(電化関連QA)</a></li>
            <li class="c-grid__item col--1of3"><a class="c-btn c-btn--secondary">簡易な機器取替やその他の<br />お問い合わせ</a></li>
            <li class="c-grid__item col--1of3"><a class="c-btn c-btn--secondary">リフォームをご検討の方は<br />こちら</a></li>
          </ul>

      </section>
    </div>
	</xsl:template>
</xsl:stylesheet>