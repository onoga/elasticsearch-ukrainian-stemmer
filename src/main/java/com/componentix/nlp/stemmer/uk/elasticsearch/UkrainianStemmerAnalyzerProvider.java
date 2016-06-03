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
import org.elasticsearch.index.settings.IndexSettingsService;

import java.io.Reader;

public class UkrainianStemmerAnalyzerProvider extends AbstractIndexAnalyzerProvider<Analyzer> {

    private final Analyzer analyzer = new StopwordAnalyzerBase() {
        @Override
        protected TokenStreamComponents createComponents(final String fieldName) {
            final Tokenizer source = new StandardTokenizer();
            TokenStream stream = new LowerCaseFilter(source);
            stream = new StopFilter(stream, stopwords);

            // TODO: use stemExclusionSet
            /*
            if (!stemExclusionSet.isEmpty()) stream = new KeywordMarkerFilter(
                stream, stemExclusionSet);
            */

            stream = new UkrainianStemmerTokenFilter(stream);
            return new TokenStreamComponents(source, stream);
        }
    };


    @Inject
    public UkrainianStemmerAnalyzerProvider(Index index, IndexSettingsService indexSettingsService,
                                             @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
    }

    @Override
    public Analyzer get() {
        return this.analyzer;
    }
}
