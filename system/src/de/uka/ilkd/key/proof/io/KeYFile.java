// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2011 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

package de.uka.ilkd.key.proof.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.uka.ilkd.key.collection.ImmutableList;
import de.uka.ilkd.key.collection.ImmutableSet;
import de.uka.ilkd.key.gui.configuration.ProofSettings;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.parser.KeYLexer;
import de.uka.ilkd.key.parser.KeYParser;
import de.uka.ilkd.key.parser.ParserConfig;
import de.uka.ilkd.key.parser.ParserMode;
import de.uka.ilkd.key.proof.CountingBufferedReader;
import de.uka.ilkd.key.proof.init.Includes;
import de.uka.ilkd.key.proof.init.InitConfig;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.mgt.SpecificationRepository;
import de.uka.ilkd.key.rule.Taclet;
import de.uka.ilkd.key.util.Debug;
import de.uka.ilkd.key.util.ProgressMonitor;


/** 
 * Represents an input from a .key file producing an environment.
 */
public class KeYFile implements EnvInput {
    
    private final String name;
    
    /** the RuleSource delivering the input stream for the file.
     */
    protected final RuleSource file;
   
    /** the graphical entity to notify on the state of reading.
     */
    protected final ProgressMonitor monitor;
    
    private String javaPath;
    private boolean javaPathAlreadyParsed=false;
    
    private InputStream input;
    
    protected InitConfig initConfig;
    
    private String chooseContract = null;

    // when parsing the key file store the classPaths here
    private ImmutableList<String> classPaths;
    
    // when parsing the key file store the boot class path here (or null if none given)
    private String bootClassPath;

    private Includes includes;
    
    //-------------------------------------------------------------------------
    //constructors
    //------------------------------------------------------------------------- 
    
    /** creates a new representation for a given file by indicating a name
     * and a RuleSource representing the physical source of the .key file.
     */
    public KeYFile(String name, 
                   RuleSource file, 
                   ProgressMonitor monitor) {
        assert name != null;
        assert file != null;
        this.name = name;
        this.file = file;
        this.monitor = monitor;
    }

        
    /** creates a new representation for a given file by indicating a name
     * and a file representing the physical source of the .key file.
     */
    public KeYFile(String name, 
                   File file, 
                   ProgressMonitor monitor) {
	this(name, RuleSource.initRuleFile(file), monitor);
    }
    

    
    //-------------------------------------------------------------------------
    //internal methods
    //-------------------------------------------------------------------------
    
    private KeYParser createDeclParser() throws FileNotFoundException {
        return new KeYParser(ParserMode.DECLARATION,
                             new KeYLexer(getNewStream(),
                                          initConfig.getServices().getExceptionHandler()),
                             file.toString(), 
                             initConfig.getServices(),
                             initConfig.namespaces());
    }


    protected InputStream getNewStream() throws FileNotFoundException {
        close();
        if (!file.isAvailable()) {
            throw new FileNotFoundException("File/Resource " + file + " not found.");  
        } 
        input = file.getNewStream();
        return input;
    }
    
    
    protected ProofSettings getPreferences() throws ProofInputException {
        if (initConfig.getSettings() == null) {
            if (file.isDirectory()) {
                return null;
            }
            try {
                KeYParser problemParser 
                    = new KeYParser(ParserMode.PROBLEM,
                                    new KeYLexer(getNewStream(), null), 
                                    file.toString());
                ProofSettings settings = new ProofSettings(ProofSettings.DEFAULT_SETTINGS);
                settings.setProfile(ProofSettings.DEFAULT_SETTINGS.getProfile());
                settings.loadSettingsFromString(problemParser.preferences());
                initConfig.setSettings(settings);
                return settings;                
            } catch (antlr.ANTLRException e) {
                throw new ProofInputException(e);
            } catch (FileNotFoundException fnfe) {
                throw new ProofInputException(fnfe);
            } catch (de.uka.ilkd.key.util.ExceptionHandlerException ehe) {
                throw new ProofInputException(ehe.getCause().getMessage());
            }
        } else {
            return initConfig.getSettings();
        }
    }
    
    
    
    //-------------------------------------------------------------------------
    //public interface
    //-------------------------------------------------------------------------
    
    @Override
    public String name() {
        return name;
    }
    
    
    @Override
    public int getNumberOfChars() {
	return file.getNumberOfChars();
    }
    
    
    @Override
        public void setInitConfig(InitConfig conf) {
        this.initConfig=conf;
    }

    
    @Override
    public Includes readIncludes() throws ProofInputException {
        if (includes == null) {
            try {
                ParserConfig pc = new ParserConfig
                (new Services(), 
                        new NamespaceSet());
                // FIXME: there is no exception handler here, thus, when parsing errors are ecountered
                // during collection of includes (it is enough to mispell \include) the error
                // message is very uninformative - ProofInputException without filename, line and column
                // numbers. Somebody please fix that. /Woj
                KeYParser problemParser = new KeYParser(ParserMode.PROBLEM, 
                        new KeYLexer(getNewStream(),
                                null), 
                                file.toString(), 
                                pc, 
                                pc, 
                                null, 
                                null); 
                problemParser.parseIncludes(); 
                includes = problemParser.getIncludes();
            } catch (antlr.ANTLRException e) {
                throw new ProofInputException(e);
            } catch (FileNotFoundException fnfe) {
                throw new ProofInputException(fnfe);
            } catch(de.uka.ilkd.key.util.ExceptionHandlerException ehe){
                throw new ProofInputException(ehe);
            }
        }
        return includes;            
    }


    @Override    
    public File readBootClassPath() {
        if(!javaPathAlreadyParsed) {
            throw new IllegalStateException("Can access this only after 'readJavaPath' has been called");
        }
        
        if(bootClassPath == null) {
            return null;
        }
        
        String parentDirectory = file.file().getParent();
        return new File(parentDirectory, bootClassPath);
    }
    

    @Override    
    public List<File> readClassPath() {
        if(!javaPathAlreadyParsed) {
            throw new IllegalStateException("Can access this only after 'readJavaPath' has been called");
        }

        String parentDirectory = file.file().getParent();
        List<File> fileList = new ArrayList<File>();
        for (String cp : classPaths) {
            if (cp == null) {
                fileList.add(null);
            } else {
                File f = new File(cp);
                if (!f.isAbsolute()) {
                    f = new File(parentDirectory, cp);
                }
                fileList.add(f);
            }
        }
        return fileList;
    }

    
    @Override    
    public String readJavaPath() throws ProofInputException {
        if (javaPathAlreadyParsed) {
            return javaPath;       
        }
        try {
            KeYParser problemParser = new KeYParser(ParserMode.PROBLEM,
                                                    new KeYLexer(getNewStream(),
                                                                 null), 
                                                    file.toString());
            
            problemParser.preferences(); // skip preferences

            bootClassPath = problemParser.bootClassPath();
            classPaths = problemParser.classPaths();
            javaPath = problemParser.javaSource(); 
            
            if(javaPath != null) {
                File cfile = new File(javaPath);
                if (!cfile.isAbsolute()) { // test relative pathname
                    File parent=file.file().getParentFile();
                    cfile = new File(parent,javaPath).
                    getCanonicalFile().getAbsoluteFile();
                    javaPath = cfile.getAbsolutePath();
                }
                if (!cfile.exists()) {
                    throw new ProofInputException("Declared Java source " 
                            + javaPath + " not found.");
                }                      
            }
            
            javaPathAlreadyParsed = true;
            
            return javaPath;
        } catch (antlr.ANTLRException e) {
            throw new ProofInputException(e);
        } catch (IOException ioe) {
            throw new ProofInputException(ioe);
        } catch(de.uka.ilkd.key.util.ExceptionHandlerException ehe){
            ehe.printStackTrace();
            System.out.println(ehe.getCause().getMessage());
            throw new ProofInputException(ehe.getCause().getMessage());
        }
    }
    
    
    @Override
    public void read() throws ProofInputException {
	if(initConfig == null) {
	    throw new IllegalStateException("KeYFile: InitConfig not set.");
	}
        
        //read .key file
	try {
            Debug.out("Reading KeY file", file);
	    CountingBufferedReader cinp = 
		new CountingBufferedReader
		    (getNewStream(),monitor,getNumberOfChars()/100);
                   
            final ParserConfig normalConfig 
                    = new ParserConfig(initConfig.getServices(), initConfig.namespaces());                       
            final ParserConfig schemaConfig 
                    = new ParserConfig(initConfig.getServices(), initConfig.namespaces());
            
            KeYParser problemParser 
                = new KeYParser(ParserMode.PROBLEM, 
                                new KeYLexer(cinp, 
                                             initConfig.getServices()
                                                       .getExceptionHandler()), 
                                             file.toString(), 
                                             schemaConfig, 
                                             normalConfig, 
                                             initConfig.getTaclet2Builder(), 
                                             initConfig.getTaclets()); 
            problemParser.problem(); 
	    initConfig.addCategory2DefaultChoices(problemParser.
						  getCategory2Default());
	    ImmutableSet<Taclet> st = problemParser.getTaclets();
	    initConfig.setTaclets(st);
            
	    SpecificationRepository specRepos 
	        = initConfig.getServices().getSpecificationRepository();
	    specRepos.addContracts(problemParser.getContracts());
	    specRepos.addClassInvariants(problemParser.getInvariants());
            chooseContract = problemParser.getChooseContract();
            Debug.out("Read KeY file   ", file);
	} catch (antlr.ANTLRException e) {
	    throw new ProofInputException(e);
	} catch (FileNotFoundException fnfe) {
	    throw new ProofInputException(fnfe);
        }
    }

    
    /** reads the sorts declaration of the .key file only, 
     * modifying the sort namespace
     * of the initial configuration 
     */
    public void readSorts() throws ProofInputException {
	try {
	    KeYParser p=createDeclParser();
	    p.parseSorts();
	    initConfig.addCategory2DefaultChoices(p.getCategory2Default());            
	} catch (antlr.ANTLRException e) {
	    throw new ProofInputException(e);
	} catch (FileNotFoundException fnfe) {
            throw new ProofInputException(fnfe);
        }
    }
    
    
    /** reads the functions and predicates declared in the .key file only, 
     * modifying the function namespaces of the respective taclet options. 
     */
    public void readFuncAndPred() throws ProofInputException {	
	if(file == null) return;
	try {
	    KeYParser p=createDeclParser();
	    p.parseFuncAndPred();
	} catch (antlr.ANTLRException e) {
	    throw new ProofInputException(e);
	} catch (FileNotFoundException fnfe) {
            throw new ProofInputException(fnfe);
        }
    }

    
   /** reads the rules and problems declared in the .key file only, 
     * modifying the set of rules 
     * of the initial configuration 
     */
    public void readRulesAndProblem() 
            throws ProofInputException {
        final ParserConfig schemaConfig = 
	    new ParserConfig(initConfig.getServices(), initConfig.namespaces());
        final ParserConfig normalConfig = 
	    new ParserConfig(initConfig.getServices(), initConfig.namespaces());
        
	try {
	    final CountingBufferedReader cinp = new CountingBufferedReader
            (getNewStream(),monitor,getNumberOfChars()/100);
            KeYParser problemParser 
                = new KeYParser(ParserMode.PROBLEM,
                                new KeYLexer(cinp, 
                                             initConfig.getServices()
                                                       .getExceptionHandler()), 
                                file.toString(),
                                schemaConfig,
                                normalConfig,
                                initConfig.getTaclet2Builder(),
                                initConfig.getTaclets());                  
            problemParser.parseTacletsAndProblem();
	    initConfig.setTaclets(problemParser.getTaclets());
	} catch (antlr.ANTLRException e) {
	    throw new ProofInputException(e);
	} catch (FileNotFoundException fnfe) {
            throw new ProofInputException(fnfe);
        }
    }

    
    public void close() {
        try {
            if (input != null) { 
		input.close();
	    }
        } catch(IOException ioe) {
            System.err.println("WARNING: Cannot close stream "+file+"\n"+ioe);
        }
    }
    
    
    public String chooseContract() {
        return chooseContract;
    }

    
    @Override    
    public String toString() {
	return name() + " " + file.toString();
    }
    
    
    @Override    
    public boolean equals(Object o) {
        if(!(o instanceof KeYFile)) {
            return false;
        }
        KeYFile kf = (KeYFile) o;
        return kf.file.getExternalForm().equals(file.getExternalForm());

    }

    
    @Override    
    public int hashCode(){
        final String externalForm = file.getExternalForm();
        if (externalForm == null) {
            return -1;
        }
	return externalForm.hashCode();
    }
}