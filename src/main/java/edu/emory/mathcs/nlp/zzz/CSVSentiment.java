/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.zzz;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.template.node.NLPNode;
import edu.emory.mathcs.nlp.decode.NLPDecoder;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CSVSentiment
{
	final NLPDecoder decode;
	
	public CSVSentiment(String configurationFile)
	{
		decode = new NLPDecoder(IOUtils.createFileInputStream(configurationFile));		
	}
	
	public void categorize(String inputFile, String outputFile) throws Exception
	{
		CSVParser parser = new CSVParser(IOUtils.createBufferedReader(inputFile), CSVFormat.DEFAULT);
		PrintStream fout = IOUtils.createBufferedPrintStream(outputFile);
		List<CSVRecord> records = parser.getRecords();
		CSVRecord record;
		NLPNode[] nodes;
		
		for (int i=0; i<records.size(); i++)
		{
			if (i == 0) continue;
			record = records.get(i);
			nodes  = decode.decode(record.get(6));
			nodes[1].putFeat("sent", record.get(0));
			fout.println(decode.toString(nodes)+"\n");
		}
		
		parser.close();
		fout.close();
	}
		
	static public void main(String[] args)
	{
		String configurationFile = "/Users/jdchoi/Documents/EmoryNLP/emorynlp/src/main/resources/configuration/config-decode-en.xml";
		String inputFile1 = "/Users/jdchoi/Documents/Data/semeval-sentiment/semeval13_T2B_16T4A_train_dev_npo.csv";
		String inputFile2 = "/Users/jdchoi/Documents/Data/semeval-sentiment/semeval16_T4A_devtest_npo.csv";
		
		try
		{
			CSVSentiment cvs = new CSVSentiment(configurationFile);
			cvs.categorize(inputFile1, inputFile1+".txt");
			cvs.categorize(inputFile2, inputFile2+".txt");
		}
		catch (Exception e) {e.printStackTrace();}
	}
}