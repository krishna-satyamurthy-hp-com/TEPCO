<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="C1028-A">
<div class="p-contract__block">

       <form>
        <section class="pb50">
          <h2 class="c-hdg__section">退会について</h2>
          <p class="mb35"><b>退会理由<span class="ml20">いずれか一つを必ずお選びください。</span></b></p>
          <ul class="c-list--col2 pb40 pl35">
		  <xsl:for-each select="//withdrawreasonselect/chkbox">
		  
            <li>
			
				<input type="checkbox">
					<xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
				</input>
				<label class="line-break">
				<xsl:attribute name="for"><xsl:value-of select="id"/></xsl:attribute>
				<xsl:value-of select="label" disable-output-escaping="yes"/></label>
              </li>
			</xsl:for-each>
			
          </ul>

          <div class="mt15 prl35">
            <textarea name="" placeholder="該当する退会理由がない場合はご入力ください。" maxlength="100"></textarea>
          </div>

        </section>

        <section class="pb25">
          <h2 class="c-hdg__section">ご注意事項</h2>
          <div class="c-frame__dash-border is-bottom pb25 mb15">
            <p class="pl35">
			<xsl:value-of select="//noteselect/label1" disable-output-escaping="yes"/></p>
          </div>

           <ul class="pl35">
            <li>
              <input type="checkbox">
			  <xsl:attribute name="id"><xsl:value-of select="//noteselect/chkbox1/id"/></xsl:attribute>
			  </input>
			  <label class="line-break"><xsl:attribute name="for"><xsl:value-of select="//noteselect/chkbox1/id"/></xsl:attribute>
			  親IDが退会すると、子IDも自動的に削除されます。<br/>(くらしのプラットフォームをご利用いただけなくなります。)</label>
            </li>
            <li>
               <input type="checkbox">
			  <xsl:attribute name="id"><xsl:value-of select="//noteselect/chkbox2/id"/></xsl:attribute>
			  </input>
			  
			  <label><xsl:attribute name="for"><xsl:value-of select="//noteselect/chkbox2/id"/></xsl:attribute>
			  ポイントがすべて消滅致します。</label>
            </li>
            <li>
              <input type="checkbox">
			  <xsl:attribute name="id"><xsl:value-of select="//noteselect/chkbox3/id"/></xsl:attribute>
			  </input>
			  
			  <label><xsl:attribute name="for"><xsl:value-of select="//noteselect/chkbox2/id"/></xsl:attribute>
			  <xsl:value-of select="//noteselect/label2" disable-output-escaping="yes"/></label>
            </li>
          </ul>
          <p class="pl35 pt5"><small><xsl:value-of select="//noteselect/label3"/></small></p>
        </section>


        <div class="c-frame__dash-border is-top pt25 c-grid u-ta--c">
          <a class="c-btn c-btn--secondary is-confirm" href="{/Properties/Data/Datum[@Name='pageLinkprev']}"><em>戻る</em></a>
          <a class="c-btn c-btn--primary is-confirm" href="{/Properties/Data/Datum[@Name='pageLink']}"><em>次へ</em></a>
        </div>
        </form>
      </div>
	
	
	
	</xsl:template>
	
</xsl:stylesheet>