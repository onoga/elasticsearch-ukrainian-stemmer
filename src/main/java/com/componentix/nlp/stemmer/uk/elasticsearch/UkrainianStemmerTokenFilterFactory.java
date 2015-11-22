package com.componentix.nlp.stemmer.uk.elasticsearch;

import com.componentix.nlp.stemmer.uk.Stemmer;
import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;

public class UkrainianStemmerTokenFilterFactory extends AbstractTokenFilterFactory {
    private final Stemmer stemmer = new Stemmer();

    @Inject
    public UkrainianStemmerTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, String name, Settings settings) {
        super(index, indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new UkrainianStemmerTokenFilter(tokenStream);
    }
}
