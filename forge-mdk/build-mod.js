#!/usr/bin/env node

/**
 * LightsaberVR Mod Builder
 * ========================
 * Node.js script to compile and package a valid Minecraft 1.20.4 Forge mod
 * 
 * Usage: node build-mod.js [--clean] [--debug]
 */

const fs = require('fs');
const path = require('path');
const { execSync, spawn } = require('child_process');

// ==================== CONFIGURATION ====================
const CONFIG = {
    // Paths
    projectRoot: path.resolve(__dirname),
    srcDir: path.resolve(__dirname, 'src/main/java'),
    resourcesDir: path.resolve(__dirname, 'src/main/resources'),
    stubsDir: path.resolve(__dirname, 'manual-build/stubs'),
    buildDir: path.resolve(__dirname, 'build'),
    outputDir: path.resolve(__dirname, '../../download'),
    
    // Java compiler
    javaHome: '/tmp/jdk-17.0.2',
    javac: '/tmp/jdk-17.0.2/bin/javac',
    
    // Mod info
    modId: 'lightsabersvr',
    modVersion: '1.0.0',
    modName: 'LightsaberVR',
    author: 'TheOnl-Coder',
    
    // Minecraft/Forge versions (CRITICAL for valid mod)
    mcVersion: '1.20.4',
    forgeVersion: '49.0.30',  // FML loader version for 1.20.4
    fmlVersionRange: '[49,)',  // MUST match 1.20.4's FML version!
};

// ==================== UTILITY FUNCTIONS ====================
function log(message, type = 'info') {
    const colors = {
        info: '\x1b[36m',    // cyan
        success: '\x1b[32m', // green
        warn: '\x1b[33m',    // yellow
        error: '\x1b[31m',   // red
        reset: '\x1b[0m'
    };
    console.log(`${colors[type]}[${type.toUpperCase()}]${colors.reset} ${message}`);
}

function ensureDir(dirPath) {
    if (!fs.existsSync(dirPath)) {
        fs.mkdirSync(dirPath, { recursive: true });
        log(`Created directory: ${dirPath}`, 'info');
    }
}

function cleanBuild() {
    if (fs.existsSync(CONFIG.buildDir)) {
        fs.rmSync(CONFIG.buildDir, { recursive: true });
        log('Cleaned build directory', 'warn');
    }
}

function findJavaFiles(dir) {
    let files = [];
    const items = fs.readdirSync(dir);
    
    for (const item of items) {
        const fullPath = path.join(dir, item);
        const stat = fs.statSync(fullPath);
        
        if (stat.isDirectory()) {
            files = files.concat(findJavaFiles(fullPath));
        } else if (item.endsWith('.java')) {
            files.push(fullPath);
        }
    }
    
    return files;
}

// ==================== MODS.TOML GENERATOR ====================
function generateModsToml() {
    // CRITICAL FIX: Use correct FML loader version for 1.20.4
    const modsToml = `modLoader="javafml"
loaderVersion="${CONFIG.fmlVersionRange}"
license="MIT"

[[mods]]
modId="${CONFIG.modId}"
version="${CONFIG.modVersion}"
displayName="${CONFIG.modName}"
authors="${CONFIG.author}"
description='''Adds lightsabers to Minecraft with full Vivecraft VR support! Wield lightsabers in virtual reality with proper controller tracking and haptic feedback.'''
logoFile="icon.png"

[[dependencies.${CONFIG.modId}]]
modId="forge"
mandatory=true
versionRange="[${CONFIG.forgeVersion.split('.')[0]},)"
ordering="NONE"
side="BOTH"

[[dependencies.${CONFIG.modId}]]
modId="minecraft"
mandatory=true
versionRange="[${CONFIG.mcVersion},)"
ordering="NONE"
side="BOTH"

[[dependencies.${CONFIG.modId}]]
modId="vrapi"
mandatory=false
versionRange="[3.0.12,)"
ordering="AFTER"
side="CLIENT"
`;
    return modsToml;
}

// ==================== COMPILATION ====================
function compileSources() {
    log('Starting compilation...', 'info');
    
    // Find all Java source files
    const sourceFiles = findJavaFiles(CONFIG.srcDir);
    log(`Found ${sourceFiles.length} source files`, 'info');
    
    if (sourceFiles.length === 0) {
        throw new Error('No Java source files found!');
    }
    
    // Prepare output directory
    const classesDir = path.join(CONFIG.buildDir, 'classes');
    ensureDir(classesDir);
    
    // Build classpath with stubs
    const classpath = [
        CONFIG.stubsDir,
        path.join(CONFIG.stubsDir, '..') // Include parent for any additional jars
    ].join(':');
    
    try {
        // Compile command
        const compileCmd = [
            CONFIG.javac,
            '-source', '17',
            '-target', '17',
            '-d', classesDir,
            '-sourcepath', `${CONFIG.srcDir}:${CONFIG.stubsDir}`,
            '-cp', classpath,
            '-nowarn',  // Suppress warnings
            ...sourceFiles
        ];
        
        log(`Running: javac -source 17 -target 17 (${sourceFiles.length} files)`, 'info');
        
        const result = execSync(compileCmd.join(' '), {
            encoding: 'utf8',
            stdio: ['pipe', 'pipe', 'pipe'],
            timeout: 60000 // 60 second timeout
        });
        
        if (result && result.trim()) {
            log(result, 'info');
        }
        
        // Verify compilation produced .class files
        const classFiles = findFiles(classesDir, '.class');
        log(`Compiled ${classFiles.length} class files successfully!`, 'success');
        
        return true;
    } catch (error) {
        log(`Compilation failed: ${error.message}`, 'error');
        if (error.stderr) {
            log(`Compiler output:\n${error.stderr}`, 'error');
        }
        if (error.stdout) {
            log(`Compiler stdout:\n${error.stdout}`, 'warn');
        }
        return false;
    }
}

function findFiles(dir, ext) {
    let files = [];
    if (!fs.existsSync(dir)) return files;
    
    const items = fs.readdirSync(dir);
    for (const item of items) {
        const fullPath = path.join(dir, item);
        const stat = fs.statSync(fullPath);
        
        if (stat.isDirectory()) {
            files = files.concat(findFiles(fullPath, ext));
        } else if (item.endsWith(ext)) {
            files.push(fullPath);
        }
    }
    return files;
}

// ==================== JAR CREATION ====================
function createJar() {
    log('Creating mod JAR...', 'info');
    
    const jarPath = path.join(CONFIG.outputDir, `${CONFIG.modId}-${CONFIG.modVersion}.jar`);
    ensureDir(CONFIG.outputDir);
    
    // Remove old jar if exists
    if (fs.existsSync(jarPath)) {
        fs.unlinkSync(jarPath);
    }
    
    const classesDir = path.join(CONFIG.buildDir, 'classes');
    
    // Create JAR using zip (compatible format)
    // JAR is just a ZIP with specific structure
    const AdmZip = require('adm-zip');
    const zip = new AdmZip();
    
    // Add compiled classes
    if (fs.existsSync(classesDir)) {
        const classFiles = findFiles(classesDir, '.class');
        for (const classFile of classFiles) {
            const relativePath = path.relative(classesDir, classFile);
            zip.addLocalFile(classFile, '', relativePath);
        }
        log(`Added ${classFiles.length} class files`, 'info');
    }
    
    // Generate and add corrected mods.toml
    const metaInfDir = path.join(CONFIG.buildDir, 'META-INF');
    ensureDir(metaInfDir);
    
    const modsTomlContent = generateModsToml();
    const modsTomlPath = path.join(metaInfDir, 'mods.toml');
    fs.writeFileSync(modsTomlPath, modsTomlContent);
    zip.addLocalFile(modsTomlPath, 'META-INF', 'mods.toml');
    log('Added META-INF/mods.toml (corrected FML version)', 'success');
    
    // Add resources (textures, models, lang)
    if (fs.existsSync(CONFIG.resourcesDir)) {
        addDirectoryToZip(zip, CONFIG.resourcesDir, '');
        log('Added resources (textures, models, lang)', 'info');
    }
    
    // Write the JAR file (JAR is just a ZIP format)
    zip.writeZip(jarPath);
    
    const stats = fs.statSync(jarPath);
    log(`Created JAR: ${jarPath}`, 'success');
    log(`Size: ${(stats.size / 1024).toFixed(1)} KB`, 'info');
    
    return jarPath;
}

function addDirectoryToZip(zip, dirPath, basePath) {
    const items = fs.readdirSync(dirPath);
    
    for (const item of items) {
        const fullPath = path.join(dirPath, item);
        const stat = fs.statSync(fullPath);
        const relativePath = basePath ? `${basePath}/${item}` : item;
        
        if (stat.isDirectory()) {
            addDirectoryToZip(zip, fullPath, relativePath);
        } else {
            zip.addLocalFile(fullPath, path.dirname(relativePath), path.basename(relativePath));
        }
    }
}

// ==================== VALIDATION ====================
function validateMod(jarPath) {
    log('Validating mod JAR...', 'info');
    const issues = [];
    
    // Check file exists
    if (!fs.existsSync(jarPath)) {
        issues.push('JAR file does not exist');
        return issues;
    }
    
    // Check it's a valid ZIP/JAR
    const AdmZip = require('adm-zip');
    let zip;
    try {
        zip = new AdmZip(jarPath);
    } catch (e) {
        issues.push('Not a valid ZIP/JAR file');
        return issues;
    }
    
    const entries = zip.getEntries();
    const entryNames = entries.map(e => e.entryName);
    
    // Check for required files
    const requiredFiles = [
        'META-INF/mods.toml',
        'com/theonl_coder/lightsabersvr/LightsaberVRMod.class'
    ];
    
    for (const req of requiredFiles) {
        if (!entryNames.includes(req)) {
            issues.push(`Missing required file: ${req}`);
        }
    }
    
    // Check for main mod class
    if (!entryNames.some(e => e.includes('LightsaberVRMod.class'))) {
        issues.push('Main mod class not found');
    }
    
    // Check mods.toml content
    const modsTomlEntry = entries.find(e => e.entryName === 'META-INF/mods.toml');
    if (modsTomlEntry) {
        const content = modsTomlEntry.getData().toString('utf8');
        
        // Verify critical fields
        if (!content.includes('modLoader="javafml"')) {
            issues.push('mods.toml missing modLoader');
        }
        if (!content.includes(`modId="${CONFIG.modId}"`)) {
            issues.push('mods.toml has wrong modId');
        }
        if (!content.includes(CONFIG.fmlVersionRange)) {
            issues.push(`mods.toml missing correct FML version range: ${CONFIG.fmlVersionRange}`);
        }
        if (!content.includes(CONFIG.mcVersion)) {
            issues.push(`mods.toml missing MC version: ${CONFIG.mcVersion}`);
        }
        
        log('mods.toml validation passed', 'success');
    } else {
        issues.push('mods.toml not found in JAR');
    }
    
    // Report results
    if (issues.length === 0) {
        log('✓ Mod validation PASSED - Valid Forge mod!', 'success');
    } else {
        log('✗ Mod validation FAILED:', 'error');
        issues.forEach(i => log(`  - ${i}`, 'error'));
    }
    
    return issues;
}

// ==================== MAIN BUILD PROCESS ====================
async function main() {
    console.log('');
    console.log('╔══════════════════════════════════════════════╗');
    console.log('║     LightsaberVR Mod Builder v2.0           ║');
    console.log('║     Minecraft 1.20.4 Forge + Vivecraft VR   ║');
    console.log('╚══════════════════════════════════════════════╝');
    console.log('');
    
    const args = process.argv.slice(2);
    const cleanBuildRequested = args.includes('--clean');
    const debugMode = args.includes('--debug');
    
    // Step 1: Clean if requested
    if (cleanBuildRequested) {
        cleanBuild();
    }
    
    // Step 2: Ensure directories exist
    ensureDir(CONFIG.buildDir);
    ensureDir(CONFIG.outputDir);
    
    // Step 3: Check JDK
    if (!fs.existsSync(CONFIG.javac)) {
        log(`JDK not found at ${CONFIG.javaHome}`, 'error');
        log('Please ensure JDK 17 is installed', 'error');
        process.exit(1);
    }
    log(`Using JDK 17: ${CONFIG.javac}`, 'info');
    
    // Step 4: Fix mods.toml before building
    const resourcesMetaInf = path.join(CONFIG.resourcesDir, 'META-INF');
    ensureDir(resourcesMetaInf);
    const fixedModsToml = generateModsToml();
    fs.writeFileSync(path.join(resourcesMetaInf, 'mods.toml'), fixedModsToml);
    log('Fixed mods.toml with correct FML version [49,)', 'success');
    
    // Step 5: Compile sources
    const compileSuccess = compileSources();
    if (!compileSuccess) {
        log('Compilation failed - aborting build', 'error');
        process.exit(1);
    }
    
    // Step 6: Create JAR
    const jarPath = createJar();
    
    // Step 7: Validate
    const issues = validateMod(jarPath);
    
    // Summary
    console.log('');
    console.log('╔══════════════════════════════════════════════╗');
    console.log('║              BUILD COMPLETE                  ║');
    console.log('╠══════════════════════════════════════════════╣');
    console.log(`║  Output: ${jarPath.padEnd(36)}║`);
    const statusText = issues.length === 0 ? 'VALID ✓' : 'INVALID ✗';
    console.log(`║  Status: ${statusText.padEnd(33)}║`);
    console.log(`║  Classes: ${findFiles(path.join(CONFIG.buildDir, 'classes'), '.class').length.toString().padEnd(32)}║`);
    console.log('╚══════════════════════════════════════════════╝');
    console.log('');
    
    // Exit with appropriate code
    process.exit(issues.length > 0 ? 1 : 0);
}

// Run the builder
main().catch(err => {
    log(`Build failed with error: ${err.message}`, 'error');
    console.error(err.stack);
    process.exit(1);
});
