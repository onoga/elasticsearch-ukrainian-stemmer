package com.componentix.nlp.stemmer.uk.elasticsearch;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.Reader;

public class UkrainianStemmerAnalyzerProvider extends AbstractIndexAnalyzerProvider<Analyzer> {

    private final Analyzer analyzer = new StopwordAnalyzerBase() {
        @Override
        protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
            final Tokenizer source = new StandardTokenizer(reader);
            TokenStream result = new LowerCaseFilter(source);
            result = new StopFilter(result, stopwords);

            // TODO: use stemExclusionSet
            /*
            if (!stemExclusionSet.isEmpty()) result = new KeywordMarkerFilter(
                result, stemExclusionSet);
            */

            result = new UkrainianStemmerTokenFilter(result);
            return new TokenStreamComponents(source, result);
        }
    };


    @Inject
    public UkrainianStemmerAnalyzerProvider(Index index, @IndexSettings Settings indexSettings,
                                             @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
    }

    @Override
    public Analyzer get() {
        return this.analyzer;
    }
}
